package app.jdgn.walletmonitor.data.local

import app.jdgn.walletmonitor.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class DatabaseSeeder(private val database: AppDatabase) {

    private val countriesQueries = database.countriesQueries
    private val currenciesQueries = database.currenciesQueries
    private val currencyTypesQueries = database.currencyTypesQueries
    private val accountTypesQueries = database.accountTypesQueries

    suspend fun seed() = withContext(Dispatchers.IO) {
        // Semilla para Account Types
        seedAccountTypes()
        
        // Solo ejecutamos si la tabla de monedas está vacía
        val currentCurrencies = currenciesQueries.selectAllCurrencies().executeAsList()
        if (currentCurrencies.isNotEmpty()) return@withContext

        database.transaction {
            val currencyData = getSeedData()
            
            // 1. Identificar tipos únicos e insertar
            val types = currencyData.map { it.type }.distinct()
            types.forEach { typeName ->
                if (currencyTypesQueries.selectTypeByName(typeName).executeAsOneOrNull() == null) {
                    currencyTypesQueries.insertCurrencyType(typeName)
                }
            }

            // 2. Identificar países únicos e insertar
            val countries = currencyData.flatMap { it.countries }.distinct()
            countries.forEach { countryAbbr ->
                if (countriesQueries.selectCountryByAbbr(countryAbbr).executeAsOneOrNull() == null) {
                    countriesQueries.insertCountry(countryAbbr, countryAbbr)
                }
            }

            // 3. Insertar monedas vinculadas a sus tipos
            currencyData.forEach { currency ->
                val typeId = currencyTypesQueries.selectTypeByName(currency.type).executeAsOne().id
                
                currenciesQueries.insertCurrency(
                    name = currency.name,
                    symbol = currency.symbol,
                    symbol_native = currency.symbol_native,
                    decimal_digits = currency.decimal_digits.toLong(),
                    code = currency.code,
                    currency_type_id = typeId,
                    countries_ids = currency.countries.joinToString(",")
                )
            }
        }
    }

    private fun seedAccountTypes() {
        val types = listOf("bank", "cash", "digital")
        database.transaction {
            types.forEach { typeName ->
                if (accountTypesQueries.selectAccountTypeByName(typeName).executeAsOneOrNull() == null) {
                    accountTypesQueries.insertAccountType(typeName)
                }
            }
        }
    }

    private fun getSeedData(): List<CurrencySeed> {
        return listOf(
            CurrencySeed("AED", "United Arab Emirates Dirham", "د.إ", 2, "AED", "fiat", listOf("AE")),
            CurrencySeed("AFN", "Afghan Afghani", "؋", 0, "Af", "fiat", listOf("AF")),
            CurrencySeed("ALL", "Albanian Lek", "Lek", 0, "ALL", "fiat", listOf("AL")),
            CurrencySeed("AMD", "Armenian Dram", "դր.", 0, "AMD", "fiat", listOf("AM")),
            CurrencySeed("ANG", "NL Antillean Guilder", "NAƒ", 2, "ƒ", "fiat", listOf()),
            CurrencySeed("AOA", "Angolan Kwanza", "Kz", 2, "Kz", "fiat", listOf("AO")),
            CurrencySeed("ARS", "Argentine Peso", "$", 2, "AR$", "fiat", listOf("AR")),
            CurrencySeed("AUD", "Australian Dollar", "$", 2, "AU$", "fiat", listOf("AU", "CC", "CX", "HM", "KI", "NF", "NR", "TV")),
            CurrencySeed("AWG", "Aruban Florin", "Afl.", 2, "Afl.", "fiat", listOf("AW")),
            CurrencySeed("AZN", "Azerbaijani Manat", "ман.", 2, "man.", "fiat", listOf("AZ")),
            CurrencySeed("BAM", "Bosnia-Herzegovina Convertible Mark", "KM", 2, "KM", "fiat", listOf("BA")),
            CurrencySeed("BBD", "Barbadian Dollar", "$", 2, "Bds$", "fiat", listOf("BB")),
            CurrencySeed("BDT", "Bangladeshi Taka", "৳", 2, "Tk", "fiat", listOf("BD")),
            CurrencySeed("BGN", "Bulgarian Lev", "лв.", 2, "BGN", "fiat", listOf()),
            CurrencySeed("BHD", "Bahraini Dinar", "د.ب.‏", 3, "BD", "fiat", listOf("BH")),
            CurrencySeed("BIF", "Burundian Franc", "FBu", 0, "FBu", "fiat", listOf("BI")),
            CurrencySeed("BMD", "Bermudan Dollar", "$", 2, "BD$", "fiat", listOf("BM")),
            CurrencySeed("BND", "Brunei Dollar", "$", 2, "BN$", "fiat", listOf("BN")),
            CurrencySeed("BOB", "Bolivian Boliviano", "Bs", 2, "Bs", "fiat", listOf("BO")),
            CurrencySeed("BRL", "Brazilian Real", "R$", 2, "R$", "fiat", listOf("BR")),
            CurrencySeed("BSD", "Bahamian Dollar", "$", 2, "B$", "fiat", listOf("BS")),
            CurrencySeed("BTN", "Bhutanese Ngultrum", "Nu.", 2, "Nu.", "fiat", listOf("BT")),
            CurrencySeed("BWP", "Botswanan Pula", "P", 2, "BWP", "fiat", listOf("BW")),
            CurrencySeed("BYN", "Belarusian ruble", "Br", 2, "Br", "fiat", listOf("BY")),
            CurrencySeed("BYR", "Belarusian Ruble", "BYR", 0, "BYR", "fiat", listOf()),
            CurrencySeed("BZD", "Belize Dollar", "$", 2, "BZ$", "fiat", listOf("BZ")),
            CurrencySeed("CAD", "Canadian Dollar", "$", 2, "CA$", "fiat", listOf("CA")),
            CurrencySeed("CDF", "Congolese Franc", "FrCD", 2, "CDF", "fiat", listOf("CD")),
            CurrencySeed("CHF", "Swiss Franc", "CHF", 2, "CHF", "fiat", listOf("CH", "LI")),
            CurrencySeed("CLF", "Unidad de Fomento", "UF", 2, "UF", "fiat", listOf("CL")),
            CurrencySeed("CLP", "Chilean Peso", "$", 0, "CL$", "fiat", listOf("CL")),
            CurrencySeed("CNY", "Chinese Yuan", "CN¥", 2, "CN¥", "fiat", listOf("CN")),
            CurrencySeed("COP", "Colombian Peso", "$", 0, "CO$", "fiat", listOf("CO")),
            CurrencySeed("CRC", "Costa Rican Colón", "₡", 0, "₡", "fiat", listOf("CR")),
            CurrencySeed("CUC", "Cuban Convertible Peso", "$", 2, "CUC$", "fiat", listOf()),
            CurrencySeed("CUP", "Cuban Peso", "$", 2, "\$MN", "fiat", listOf("CU")),
            CurrencySeed("CVE", "Cape Verdean Escudo", "CV$", 2, "CV$", "fiat", listOf("CV")),
            CurrencySeed("CZK", "Czech Republic Koruna", "Kč", 2, "Kč", "fiat", listOf("CZ")),
            CurrencySeed("DJF", "Djiboutian Franc", "Fdj", 0, "Fdj", "fiat", listOf("DJ")),
            CurrencySeed("DKK", "Danish Krone", "kr", 2, "Dkr", "fiat", listOf("DK", "FO", "GL")),
            CurrencySeed("DOP", "Dominican Peso", "RD$", 2, "RD$", "fiat", listOf("DO")),
            CurrencySeed("DZD", "Algerian Dinar", "د.ج.‏", 2, "DA", "fiat", listOf("DZ")),
            CurrencySeed("EGP", "Egyptian Pound", "ج.m.‏", 2, "EGP", "fiat", listOf("EG", "PS")),
            CurrencySeed("ERN", "Eritrean Nakfa", "Nfk", 2, "Nfk", "fiat", listOf("ER")),
            CurrencySeed("ETB", "Ethiopian Birr", "Br", 2, "Br", "fiat", listOf("ET")),
            CurrencySeed("EUR", "Euro", "€", 2, "€", "fiat", listOf("AD", "AT", "AX", "BE", "BG", "BL", "CP", "CY", "DE", "EA", "EE", "ES", "EU", "FI", "FR", "FX", "GF", "GP", "GR", "HR", "IC", "IE", "IT", "LT", "LU", "LV", "MC", "ME", "MF", "MQ", "MT", "NL", "PM", "PT", "RE", "SI", "SK", "SM", "TF", "VA", "XK", "YT")),
            CurrencySeed("FJD", "Fijian Dollar", "$", 2, "FJ$", "fiat", listOf("FJ")),
            CurrencySeed("FKP", "Falkland Islands Pound", "£", 2, "FK£", "fiat", listOf("FK")),
            CurrencySeed("GBP", "British Pound Sterling", "£", 2, "£", "fiat", listOf("GB", "GG", "GS", "IM", "JE", "TA", "UK")),
            CurrencySeed("GEL", "Georgian Lari", "GEL", 2, "GEL", "fiat", listOf("GE")),
            CurrencySeed("GGP", "Guernsey pound", "£", 2, "£", "fiat", listOf()),
            CurrencySeed("GHS", "Ghanaian Cedi", "GH₵", 2, "GH₵", "fiat", listOf("GH")),
            CurrencySeed("GIP", "Gibraltar Pound", "£", 2, "£", "fiat", listOf("GI")),
            CurrencySeed("GMD", "Gambian Dalasi", "D", 2, "D", "fiat", listOf("GM")),
            CurrencySeed("GNF", "Guinean Franc", "FG", 0, "FG", "fiat", listOf("GN")),
            CurrencySeed("GTQ", "Guatemalan Quetzal", "Q", 2, "GTQ", "fiat", listOf("GT")),
            CurrencySeed("GYD", "Guyanaese Dollar", "$", 2, "G$", "fiat", listOf("GY")),
            CurrencySeed("HKD", "Hong Kong Dollar", "$", 2, "HK$", "fiat", listOf("HK")),
            CurrencySeed("HNL", "Honduran Lempira", "L", 2, "HNL", "fiat", listOf("HN")),
            CurrencySeed("HRK", "Croatian Kuna", "kn", 2, "kn", "fiat", listOf()),
            CurrencySeed("HTG", "Haitian Gourde", "G", 2, "G", "fiat", listOf("HT")),
            CurrencySeed("HUF", "Hungarian Forint", "Ft", 0, "Ft", "fiat", listOf("HU")),
            CurrencySeed("IDR", "Indonesian Rupiah", "Rp", 0, "Rp", "fiat", listOf("ID")),
            CurrencySeed("ILS", "Israeli New Sheqel", "₪", 2, "₪", "fiat", listOf("IL", "PS")),
            CurrencySeed("IMP", "Manx pound", "£", 2, "£", "fiat", listOf()),
            CurrencySeed("INR", "Indian Rupee", "টকা", 2, "Rs", "fiat", listOf("BT", "IN")),
            CurrencySeed("IQD", "Iraqi Dinar", "د.ع.‏", 0, "IQD", "fiat", listOf("IQ")),
            CurrencySeed("IRR", "Iranian Rial", "﷼", 0, "IRR", "fiat", listOf("IR")),
            CurrencySeed("ISK", "Icelandic Króna", "kr", 0, "Ikr", "fiat", listOf("IS")),
            CurrencySeed("JEP", "Jersey pound", "£", 2, "£", "fiat", listOf()),
            CurrencySeed("JMD", "Jamaican Dollar", "$", 2, "J$", "fiat", listOf("JM")),
            CurrencySeed("JOD", "Jordanian Dinar", "د.أ.‏", 3, "JD", "fiat", listOf("JO", "PS")),
            CurrencySeed("JPY", "Japanese Yen", "￥", 0, "¥", "fiat", listOf("JP")),
            CurrencySeed("KES", "Kenyan Shilling", "Ksh", 2, "Ksh", "fiat", listOf("KE")),
            CurrencySeed("KGS", "Kyrgystani Som", "KGS", 2, "KGS", "fiat", listOf("KG")),
            CurrencySeed("KHR", "Cambodian Riel", "៛", 2, "KHR", "fiat", listOf("KH")),
            CurrencySeed("KMF", "Comorian Franc", "FC", 0, "CF", "fiat", listOf("KM")),
            CurrencySeed("KPW", "North Korean Won", "₩", 2, "₩", "fiat", listOf("KP")),
            CurrencySeed("KRW", "South Korean Won", "₩", 0, "₩", "fiat", listOf("KR")),
            CurrencySeed("KWD", "Kuwaiti Dinar", "د.ك.‏", 3, "KD", "fiat", listOf("KW")),
            CurrencySeed("KYD", "Cayman Islands Dollar", "$‏", 2, "CI$", "fiat", listOf("KY")),
            CurrencySeed("KZT", "Kazakhstani Tenge", "тңг.", 2, "KZT", "fiat", listOf("KZ")),
            CurrencySeed("LAK", "Laotian Kip", "₭‏‏", 0, "₭N", "fiat", listOf("LA")),
            CurrencySeed("LBP", "Lebanese Pound", "ل.ل.‏", 0, "LB£", "fiat", listOf("LB")),
            CurrencySeed("LKR", "Sri Lankan Rupee", "SL Re", 2, "SLRs", "fiat", listOf("LK")),
            CurrencySeed("LRD", "Liberian Dollar", "L$", 2, "LD$", "fiat", listOf("LR")),
            CurrencySeed("LSL", "Lesotho Loti", "M", 2, "L", "fiat", listOf("LS")),
            CurrencySeed("LTL", "Lithuanian Litas", "Lt", 2, "Lt", "fiat", listOf()),
            CurrencySeed("LVL", "Latvian Lats", "Ls", 2, "Ls", "fiat", listOf()),
            CurrencySeed("LYD", "Libyan Dinar", "د.l.‏", 3, "LD", "fiat", listOf("LY")),
            CurrencySeed("MAD", "Moroccan Dirham", "د.m.‏", 2, "MAD", "fiat", listOf("EH", "MA")),
            CurrencySeed("MDL", "Moldovan Leu", "MDL", 2, "MDL", "fiat", listOf("MD")),
            CurrencySeed("MGA", "Malagasy Ariary", "MGA", 0, "MGA", "fiat", listOf("MG")),
            CurrencySeed("MKD", "Macedonian Denar", "MKD", 2, "MKD", "fiat", listOf("MK")),
            CurrencySeed("MMK", "Myanma Kyat", "K", 0, "MMK", "fiat", listOf("MM")),
            CurrencySeed("MNT", "Mongolian Tugrik", "₮", 2, "₮", "fiat", listOf("MN")),
            CurrencySeed("MOP", "Macanese Pataca", "MOP$", 2, "MOP$", "fiat", listOf("MO")),
            CurrencySeed("MRO", "Mauritanian ouguiya", "UM", 2, "UM", "fiat", listOf()),
            CurrencySeed("MUR", "Mauritian Rupee", "MURs", 0, "MURs", "fiat", listOf("MU")),
            CurrencySeed("MVR", "Maldivian Rufiyaa", "Rf", 2, "MRf", "fiat", listOf("MV")),
            CurrencySeed("MWK", "Malawian Kwacha", "MK", 2, "MK", "fiat", listOf("MW")),
            CurrencySeed("MXN", "Mexican Peso", "$", 2, "MX$", "fiat", listOf("MX")),
            CurrencySeed("MYR", "Malaysian Ringgit", "RM", 2, "RM", "fiat", listOf("MY")),
            CurrencySeed("MZN", "Mozambican Metical", "MTn", 2, "MTn", "fiat", listOf("MZ")),
            CurrencySeed("NAD", "Namibian Dollar", "N$", 2, "N$", "fiat", listOf("NA")),
            CurrencySeed("NGN", "Nigerian Naira", "₦", 2, "₦", "fiat", listOf("NG")),
            CurrencySeed("NIO", "Nicaraguan Córdoba", "C$", 2, "C$", "fiat", listOf("NI")),
            CurrencySeed("NOK", "Norwegian Krone", "kr", 2, "Nkr", "fiat", listOf("BV", "NO", "SJ")),
            CurrencySeed("NPR", "Nepalese Rupee", "नेरू", 2, "NPRs", "fiat", listOf("NP")),
            CurrencySeed("NZD", "New Zealand Dollar", "$", 2, "NZ$", "fiat", listOf("CK", "NU", "NZ", "PN", "TK")),
            CurrencySeed("OMR", "Omani Rial", "ر.ع.‏", 3, "OMR", "fiat", listOf("OM")),
            CurrencySeed("PAB", "Panamanian Balboa", "B/.", 2, "B/.", "fiat", listOf("PA")),
            CurrencySeed("PEN", "Peruvian Nuevo Sol", "S/.", 2, "S/.", "fiat", listOf("PE")),
            CurrencySeed("PGK", "Papua New Guinean Kina", "K", 2, "K", "fiat", listOf("PG")),
            CurrencySeed("PHP", "Philippine Peso", "₱", 2, "₱", "fiat", listOf("PH")),
            CurrencySeed("PKR", "Pakistani Rupee", "₨", 0, "PKRs", "fiat", listOf("PK")),
            CurrencySeed("PLN", "Polish Zloty", "zł", 2, "zł", "fiat", listOf("PL")),
            CurrencySeed("PYG", "Paraguayan Guarani", "₲", 0, "₲", "fiat", listOf("PY")),
            CurrencySeed("QAR", "Qatari Rial", "ر.ق.‏", 2, "QR", "fiat", listOf("QA")),
            CurrencySeed("RON", "Romanian Leu", "RON", 2, "RON", "fiat", listOf("RO")),
            CurrencySeed("RSD", "Serbian Dinar", "дин.", 0, "din.", "fiat", listOf("RS")),
            CurrencySeed("RUB", "Russian Ruble", "руб.", 2, "RUB", "fiat", listOf("RU", "SU")),
            CurrencySeed("RWF", "Rwandan Franc", "FR", 0, "RWF", "fiat", listOf("RW")),
            CurrencySeed("SAR", "Saudi Riyal", "ر.s.‏", 2, "SR", "fiat", listOf("SA")),
            CurrencySeed("SBD", "Solomon Islands Dollar", "$", 2, "SI$", "fiat", listOf("SB")),
            CurrencySeed("SCR", "Seychellois Rupee", "SR", 2, "SRe", "fiat", listOf("SC")),
            CurrencySeed("SDG", "Sudanese Pound", "SDG", 2, "SDG", "fiat", listOf("SD")),
            CurrencySeed("SEK", "Swedish Krona", "kr", 2, "Skr", "fiat", listOf("SE")),
            CurrencySeed("SGD", "Singapore Dollar", "$", 2, "S$", "fiat", listOf("SG")),
            CurrencySeed("SHP", "Saint Helena Pound", "£", 2, "£", "fiat", listOf("SH")),
            CurrencySeed("SLL", "Sierra Leonean Leone", "Le", 2, "Le", "fiat", listOf()),
            CurrencySeed("SOS", "Somali Shilling", "Ssh", 0, "Ssh", "fiat", listOf("SO")),
            CurrencySeed("SRD", "Surinamese Dollar", "$", 2, "\$SUR", "fiat", listOf("SR")),
            CurrencySeed("STD", "São Tomé and Príncipe dobra", "Db", 2, "Db", "fiat", listOf()),
            CurrencySeed("SVC", "Salvadoran Colón", "₡", 2, "₡", "fiat", listOf()),
            CurrencySeed("SYP", "Syrian Pound", "ل.س.‏", 0, "SY£", "fiat", listOf("SY")),
            CurrencySeed("SZL", "Swazi Lilangeni", "E‏", 2, "L", "fiat", listOf("SZ")),
            CurrencySeed("THB", "Thai Baht", "฿", 2, "฿", "fiat", listOf("TH")),
            CurrencySeed("TJS", "Tajikistani Somoni", "TJS", 2, "TJS", "fiat", listOf("TJ")),
            CurrencySeed("TMT", "Turkmenistani Manat", "T‏", 2, "T", "fiat", listOf("TM")),
            CurrencySeed("TND", "Tunisian Dinar", "د.ت.‏", 3, "DT", "fiat", listOf("TN")),
            CurrencySeed("TOP", "Tongan Paʻanga", "T$", 2, "T$", "fiat", listOf("TO")),
            CurrencySeed("TRY", "Turkish Lira", "₺", 2, "TL", "fiat", listOf("TR")),
            CurrencySeed("TTD", "Trinidad and Tobago Dollar", "$", 2, "TT$", "fiat", listOf("TT")),
            CurrencySeed("TWD", "New Taiwan Dollar", "NT$", 2, "NT$", "fiat", listOf("TW")),
            CurrencySeed("TZS", "Tanzanian Shilling", "TSh", 0, "TSh", "fiat", listOf("TZ")),
            CurrencySeed("UAH", "Ukrainian Hryvnia", "₴", 2, "₴", "fiat", listOf("UA")),
            CurrencySeed("UGX", "Ugandan Shilling", "USh", 0, "USh", "fiat", listOf("UG")),
            CurrencySeed("USD", "US Dollar", "$", 2, "$", "fiat", listOf("AC", "AS", "BQ", "DG", "EC", "FM", "GU", "HT", "IO", "MH", "MP", "PA", "PR", "PW", "SV", "TC", "TL", "UM", "US", "VG", "VI")),
            CurrencySeed("UYU", "Uruguayan Peso", "$", 2, "\$U", "fiat", listOf("UY")),
            CurrencySeed("UZS", "Uzbekistan Som", "UZS", 0, "UZS", "fiat", listOf("UZ")),
            CurrencySeed("VEF", "Venezuelan Bolívar", "Bs.F.", 2, "Bs.F.", "fiat", listOf()),
            CurrencySeed("VND", "Vietnamese Dong", "₫", 0, "₫", "fiat", listOf("VN")),
            CurrencySeed("VUV", "Vanuatu Vatu", "VT", 0, "VUV", "fiat", listOf("VU")),
            CurrencySeed("WST", "Samoan Tala", "T", 2, "WS$", "fiat", listOf("WS")),
            CurrencySeed("XAF", "CFA Franc BEAC", "FCFA", 0, "FCFA", "fiat", listOf("CF", "CG", "CM", "GA", "GQ", "TD")),
            CurrencySeed("XAG", "Silver Ounce", "XAG", 2, "XAG", "metal", listOf()),
            CurrencySeed("XAU", "Gold Ounce", "XAU", 2, "XAU", "metal", listOf()),
            CurrencySeed("XCD", "East Caribbean Dollar", "$", 2, "EC$", "fiat", listOf("AG", "AI", "DM", "GD", "KN", "LC", "MS", "VC")),
            CurrencySeed("XDR", "Special drawing rights", "SDR", 2, "SDR", "fiat", listOf()),
            CurrencySeed("XOF", "CFA Franc BCEAO", "CFA", 0, "CFA", "fiat", listOf("BF", "BJ", "CI", "GW", "ML", "NE", "SN", "TG")),
            CurrencySeed("XPF", "CFP Franc", "CFP", 0, "CFP", "fiat", listOf("NC", "PF", "WF")),
            CurrencySeed("YER", "Yemeni Rial", "ر.ي.‏", 0, "YR", "fiat", listOf("YE")),
            CurrencySeed("ZAR", "South African Rand", "R", 2, "R", "fiat", listOf("LS", "NA", "ZA")),
            CurrencySeed("ZMK", "Zambian Kwacha", "ZK", 0, "ZK", "fiat", listOf()),
            CurrencySeed("ZMW", "Zambian Kwacha", "ZK", 0, "ZK", "fiat", listOf("ZM")),
            CurrencySeed("ZWL", "Zimbabwean dollar", "$", 2, "ZWL", "fiat", listOf()),
            CurrencySeed("XPT", "Platinum Ounce", "XPT", 6, "XPT", "metal", listOf()),
            CurrencySeed("XPD", "Palladium Ounce", "XPD", 6, "XPD", "metal", listOf()),
            CurrencySeed("BTC", "Bitcoin", "₿", 8, "₿", "crypto", listOf()),
            CurrencySeed("ETH", "Ethereum", "Ξ", 18, "Ξ", "crypto", listOf()),
            CurrencySeed("BNB", "Binance", "BNB", 8, "BNB", "crypto", listOf()),
            CurrencySeed("XRP", "Ripple", "XRP", 6, "XRP", "crypto", listOf()),
            CurrencySeed("SOL", "Solana", "SOL", 9, "SOL", "crypto", listOf()),
            CurrencySeed("DOT", "Polkadot", "DOT", 10, "DOT", "crypto", listOf()),
            CurrencySeed("AVAX", "Avalanche", "AVAX", 18, "AVAX", "crypto", listOf()),
            CurrencySeed("MATIC", "Matic Token", "MATIC", 18, "MATIC", "crypto", listOf()),
            CurrencySeed("LTC", "Litecoin", "Ł", 8, "Ł", "crypto", listOf()),
            CurrencySeed("ADA", "Cardano", "ADA", 6, "ADA", "crypto", listOf()),
            CurrencySeed("USDT", "Tether", "USDT", 2, "USDT", "crypto", listOf()),
            CurrencySeed("USDC", "USD Coin", "USDC", 2, "USDC", "crypto", listOf()),
            CurrencySeed("DAI", "Dai", "DAI", 2, "DAI", "crypto", listOf()),
            CurrencySeed("ARB", "Arbitrum", "ARB", 8, "ARB", "crypto", listOf()),
            CurrencySeed("OP", "Optimism", "OP", 8, "OP", "crypto", listOf()),
            CurrencySeed("TRX", "Tron", "TRX", 6, "TRX", "crypto", listOf()),
            CurrencySeed("VES", "Venezuelan Bolívar", "Bs.S.", 2, "Bs.S.", "fiat", listOf("VE")),
            CurrencySeed("STN", "São Tomé and Príncipe dobra", "STN", 2, "STN", "fiat", listOf("ST")),
            CurrencySeed("MRU", "Mauritanian ouguiya", "MRU", 2, "MRU", "fiat", listOf("MR")),
            CurrencySeed("ZWG", "Zimbabwe Gold", "ZWG", 2, "ZWG", "fiat", listOf("ZW")),
            CurrencySeed("SLE", "Sierra Leonean leone", "SLE", 2, "SLE", "fiat", listOf("SL")),
            CurrencySeed("XCG", "Caribbean guilder", "XCG", 2, "XCG", "fiat", listOf("CW", "SX"))
        )
    }

    private data class CurrencySeed(
        val code: String,
        val name: String,
        val symbol_native: String,
        val decimal_digits: Int,
        val symbol: String,
        val type: String,
        val countries: List<String>
    )
}
