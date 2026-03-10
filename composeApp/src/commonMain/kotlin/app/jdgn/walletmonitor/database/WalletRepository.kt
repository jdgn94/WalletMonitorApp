package app.jdgn.walletmonitor.database

import kotlinx.coroutines.flow.Flow
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class WalletRepository(database: AppDatabase) {
    private val countriesQueries = database.countriesQueries
    private val currenciesQueries = database.currenciesQueries
    private val banksQueries = database.banksQueries
    private val exchangeRatesQueries = database.exchangeRatesQueries
    private val currencyTypesQueries = database.currencyTypesQueries

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

    fun getAllBanks(): Flow<List<Banks>> {
        return banksQueries.selectAllBanks()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
}
