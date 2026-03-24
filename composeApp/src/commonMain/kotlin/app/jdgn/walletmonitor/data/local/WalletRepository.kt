package app.jdgn.walletmonitor.data.local

import kotlinx.coroutines.flow.Flow
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.jdgn.walletmonitor.database.AppDatabase
import app.jdgn.walletmonitor.database.Banks
import app.jdgn.walletmonitor.database.Countries
import app.jdgn.walletmonitor.database.Currencies
import app.jdgn.walletmonitor.database.Account_types
import app.jdgn.walletmonitor.database.Accounts
import app.jdgn.walletmonitor.database.SelectAllCurrenciesWithType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class WalletRepository(database: AppDatabase) {
    private val countriesQueries = database.countriesQueries
    private val currenciesQueries = database.currenciesQueries
    private val banksQueries = database.banksQueries
    private val exchangeRatesQueries = database.exchangeRatesQueries
    private val currencyTypesQueries = database.currencyTypesQueries
    private val accountTypesQueries = database.accountTypesQueries
    private val accountsQueries = database.accountsQueries


    fun getAllCountries(): Flow<List<Countries>> {
        return countriesQueries.selectAllCountries()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    fun insertCountry(name: String, abbr: String) {
        countriesQueries.insertCountry(name, abbr)
    }

    fun getAllCurrencies(): Flow<List<Currencies>> {
        return currenciesQueries.selectAllCurrencies()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    fun getAllCurrenciesWithType(): Flow<List<SelectAllCurrenciesWithType>> {
        return currenciesQueries.selectAllCurrenciesWithType()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    fun getCurrencyById(id: Long): Flow<Currencies?> {
        return currenciesQueries.selectCurrencyById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }

    fun getAllBanks(): Flow<List<Banks>> {
        return banksQueries.selectAllBanks()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    fun getAllAccountTypes(): Flow<List<Account_types>> {
        return accountTypesQueries.selectAllAccountTypes()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    fun getAllAccounts(): Flow<List<Accounts>> {
        return accountsQueries.selectAllAccounts()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    fun insertAccount(
        bankId: Long?,
        name: String,
        icon: String,
        accountTypeId: Long,
        currencyId: Long,
        color: String,
        amount: Double
    ) {
        accountsQueries.insertAccount(bankId, name, icon, accountTypeId, currencyId, color, amount)
    }
}
