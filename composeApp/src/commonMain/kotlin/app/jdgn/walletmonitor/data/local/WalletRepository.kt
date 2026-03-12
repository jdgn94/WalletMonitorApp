package app.jdgn.walletmonitor.data.local

import kotlinx.coroutines.flow.Flow
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.jdgn.walletmonitor.database.AppDatabase
import app.jdgn.walletmonitor.database.Banks
import app.jdgn.walletmonitor.database.Countries
import app.jdgn.walletmonitor.database.Currencies
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
}
