package app.jdgn.walletmonitor.utils

object NumberUtils {
    /**
     * Formatea un número con decimales fijos y separador de miles.
     * Ejemplo: 1234.5 -> 1,234.50
     */
    fun formatNumber(number: Double, decimals: Int = 2): String {
        val parts = number.toString().split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else ""
        
        val paddedFractional = fractionalPart.padEnd(decimals, '0').take(decimals)
        
        // Formateo de miles manual para KMP
        val reversedInteger = integerPart.reversed()
        val withCommas = StringBuilder()
        for (i in reversedInteger.indices) {
            if (i > 0 && i % 3 == 0 && reversedInteger[i] != '-') {
                withCommas.append(",")
            }
            withCommas.append(reversedInteger[i])
        }
        
        return if (decimals > 0) {
            "${withCommas.reverse()}.$paddedFractional"
        } else {
            withCommas.reverse().toString()
        }
    }

    /**
     * Formatea como moneda agregando un símbolo.
     */
    fun formatCurrency(number: Double, symbol: String = "$"): String {
        return "$symbol ${formatNumber(number)}"
    }
}
