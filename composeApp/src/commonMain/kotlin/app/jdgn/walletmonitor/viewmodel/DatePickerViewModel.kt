package app.jdgn.walletmonitor.viewmodel

import app.jdgn.walletmonitor.ui.components.form.DateGranularity
import app.jdgn.walletmonitor.ui.components.form.DateSelectionMode
import app.jdgn.walletmonitor.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.*

data class DatePickerState(
    val selectedDates: List<LocalDate> = emptyList(),
    val mode: DateSelectionMode = DateSelectionMode.SINGLE,
    val granularity: DateGranularity = DateGranularity.DAY,
    val currentMonth: LocalDate = DateUtils.today(),
    val viewingYear: Int = DateUtils.today().year,
    val viewState: DatePickerViewState = DatePickerViewState.DAYS,
    val formattedResult: String = "",
    val minDate: LocalDate = LocalDate(2020, 1, 1),
    val maxDate: LocalDate = DateUtils.today()
)

enum class DatePickerViewState { DAYS, MONTHS, YEARS }

class DatePickerViewModel : ViewModel() {
    private val _state = MutableStateFlow(DatePickerState())
    val state: StateFlow<DatePickerState> = _state.asStateFlow()

    fun initialize(
        initialSelectedDates: List<LocalDate>,
        mode: DateSelectionMode,
        granularity: DateGranularity,
        minDate: LocalDate? = null,
        maxDate: LocalDate? = null
    ) {
        val today = DateUtils.today()
        val initialMonth = initialSelectedDates.firstOrNull() ?: today
        
        _state.value = DatePickerState(
            selectedDates = initialSelectedDates,
            mode = mode,
            granularity = granularity,
            currentMonth = initialMonth,
            viewingYear = initialMonth.year,
            viewState = when (granularity) {
                DateGranularity.YEAR -> DatePickerViewState.YEARS
                DateGranularity.MONTH -> DatePickerViewState.MONTHS
                DateGranularity.DAY -> DatePickerViewState.DAYS
            },
            formattedResult = formatDates(initialSelectedDates, granularity, mode),
            minDate = minDate ?: LocalDate(2020, 1, 1),
            maxDate = maxDate ?: today
        )
    }

    fun setMode(mode: DateSelectionMode) {
        _state.value = _state.value.copy(
            mode = mode, 
            selectedDates = emptyList(),
            formattedResult = ""
        )
    }

    fun onDateClick(date: LocalDate) {
        if (date < _state.value.minDate || date > _state.value.maxDate) return

        val current = _state.value.selectedDates
        val mode = _state.value.mode
        val granularity = _state.value.granularity
        
        val newSelected = when (mode) {
            DateSelectionMode.SINGLE -> listOf(date)
            DateSelectionMode.MULTIPLE -> {
                if (current.contains(date)) current.filter { it != date }
                else (current + date).sorted()
            }
            DateSelectionMode.RANGE -> {
                if (current.size != 1) listOf(date)
                else (current + date).sorted()
            }
        }
        
        _state.value = _state.value.copy(
            selectedDates = newSelected,
            formattedResult = formatDates(newSelected, granularity, mode)
        )
    }

    fun onMonthClick(date: LocalDate) {
        if (_state.value.granularity == DateGranularity.MONTH) {
            // Ensure month is within range (at least some part of it)
            val firstOfMonth = LocalDate(date.year, date.month, 1)
            val lastOfMonth = LocalDate(date.year, date.month, date.month.length(isLeapYear(date.year)))
            if (lastOfMonth < _state.value.minDate || firstOfMonth > _state.value.maxDate) return
            
            onDateClick(date)
        } else {
            _state.value = _state.value.copy(
                currentMonth = date,
                viewState = DatePickerViewState.DAYS
            )
        }
    }

    fun onYearClick(date: LocalDate) {
        if (_state.value.granularity == DateGranularity.YEAR) {
            if (date.year < _state.value.minDate.year || date.year > _state.value.maxDate.year) return
            onDateClick(date)
        } else {
            _state.value = _state.value.copy(
                viewingYear = date.year,
                viewState = DatePickerViewState.MONTHS
            )
        }
    }

    fun navigatePrevious() {
        val s = _state.value
        when (s.viewState) {
            DatePickerViewState.DAYS -> _state.value = s.copy(currentMonth = s.currentMonth.minus(1, DateTimeUnit.MONTH))
            DatePickerViewState.MONTHS -> _state.value = s.copy(viewingYear = s.viewingYear - 1)
            DatePickerViewState.YEARS -> _state.value = s.copy(viewingYear = s.viewingYear - 20)
        }
    }

    fun navigateNext() {
        val s = _state.value
        when (s.viewState) {
            DatePickerViewState.DAYS -> _state.value = s.copy(currentMonth = s.currentMonth.plus(1, DateTimeUnit.MONTH))
            DatePickerViewState.MONTHS -> _state.value = s.copy(viewingYear = s.viewingYear + 1)
            DatePickerViewState.YEARS -> _state.value = s.copy(viewingYear = s.viewingYear + 20)
        }
    }

    fun switchToMonthsView() {
        _state.value = _state.value.copy(viewState = DatePickerViewState.MONTHS)
    }

    fun switchToYearsView() {
        _state.value = _state.value.copy(viewState = DatePickerViewState.YEARS)
    }

    private fun formatDates(dates: List<LocalDate>, granularity: DateGranularity, mode: DateSelectionMode): String {
        if (dates.isEmpty()) return ""
        val sorted = dates.sorted()
        
        return when (granularity) {
            DateGranularity.YEAR -> sorted.joinToString(", ") { DateUtils.formatLocalDate(it, "yyyy") }
            DateGranularity.MONTH -> sorted.joinToString(", ") { DateUtils.formatLocalDate(it, "MMM yyyy") }
            DateGranularity.DAY -> {
                if (mode == DateSelectionMode.RANGE && sorted.size >= 2) {
                    "${DateUtils.formatLocalDate(sorted.first())} - ${DateUtils.formatLocalDate(sorted.last())}"
                } else {
                    sorted.joinToString(", ") { DateUtils.formatLocalDate(it) }
                }
            }
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }

    private fun Month.length(isLeapYear: Boolean): Int {
        return when (this) {
            Month.FEBRUARY -> if (isLeapYear) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
    }
}
