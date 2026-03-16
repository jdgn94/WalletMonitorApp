package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.dialogs.CustomDialog
import app.jdgn.walletmonitor.viewmodel.DatePickerViewModel
import app.jdgn.walletmonitor.viewmodel.DatePickerViewState
import app.jdgn.walletmonitor.viewmodel.rememberViewModel
import kotlinx.datetime.*

enum class DateSelectionMode {
    SINGLE,
    MULTIPLE,
    RANGE
}

enum class DateGranularity {
    YEAR,
    MONTH,
    DAY
}

@Composable
fun DatePickerField(
    label: String,
    selectedDates: List<LocalDate>,
    onDatesSelected: (List<LocalDate>, String) -> Unit,
    modifier: Modifier = Modifier,
    initialMode: DateSelectionMode = DateSelectionMode.SINGLE,
    granularity: DateGranularity = DateGranularity.DAY,
    isSwitchable: Boolean = false,
    customThemeColor: Color? = null,
    customContainerColor: Color? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var currentMode by remember { mutableStateOf(initialMode) }
    
    val themeColor = customThemeColor ?: MaterialTheme.colorScheme.primary
    val backgroundColor = customContainerColor ?: MaterialTheme.colorScheme.surface
    val errorColor = MaterialTheme.colorScheme.error
    val shadowColor = if (isError) errorColor else themeColor

    val displayText = remember(selectedDates, granularity, currentMode) {
        if (selectedDates.isEmpty()) placeholder ?: ""
        else {
            formatDateList(selectedDates, granularity, currentMode)
        }
    }

    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDialog = true },
            padding = 0.dp,
            shadowElevation = if (isError) 6.dp else 4.dp,
            shadowColor = shadowColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
            ) {
                if (label.isNotEmpty()) {
                    Text(
                        text = label,
                        color = shadowColor,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedDates.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) 
                            else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 10.dp)
                )
                
                Icon(
                    imageVector = if (currentMode == DateSelectionMode.RANGE) Icons.Default.DateRange else Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = shadowColor,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = errorColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }

    if (showDialog) {
        CalendarDialog(
            initialSelectedDates = selectedDates,
            mode = currentMode,
            granularity = granularity,
            isSwitchable = isSwitchable,
            themeColor = themeColor,
            minDate = minDate,
            maxDate = maxDate,
            onDismiss = { showDialog = false },
            onModeChanged = { currentMode = it },
            onConfirmed = { dates ->
                onDatesSelected(dates, formatDateList(dates, granularity, currentMode))
                showDialog = false
            }
        )
    }
}

private fun formatDateList(dates: List<LocalDate>, granularity: DateGranularity, mode: DateSelectionMode): String {
    val sorted = dates.sorted()
    return when (granularity) {
        DateGranularity.YEAR -> sorted.map { it.year.toString() }.joinToString(", ")
        DateGranularity.MONTH -> sorted.map { "${it.month.name.lowercase().take(3).capitalize()} ${it.year}" }.joinToString(", ")
        DateGranularity.DAY -> {
            if (mode == DateSelectionMode.RANGE && sorted.size >= 2) {
                "${sorted.minOrNull()} - ${sorted.maxOrNull()}"
            } else {
                sorted.joinToString(", ")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    initialSelectedDates: List<LocalDate>,
    mode: DateSelectionMode,
    granularity: DateGranularity,
    isSwitchable: Boolean,
    themeColor: Color,
    onDismiss: () -> Unit,
    onModeChanged: (DateSelectionMode) -> Unit,
    onConfirmed: (List<LocalDate>) -> Unit,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    viewModel: DatePickerViewModel = rememberViewModel { DatePickerViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.initialize(initialSelectedDates, mode, granularity, minDate, maxDate)
    }

    val state by viewModel.state.collectAsState()

    CustomDialog(
        onDismissRequest = onDismiss,
        shadowColor = themeColor,
        header = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when(state.viewState) {
                            DatePickerViewState.DAYS -> "${state.currentMonth.month.name.lowercase().capitalize()} ${state.currentMonth.year}"
                            DatePickerViewState.MONTHS -> "${state.viewingYear}"
                            DatePickerViewState.YEARS -> "${state.viewingYear - 10} - ${state.viewingYear + 10}"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = themeColor,
                        modifier = Modifier.clickable {
                            when(state.viewState) {
                                DatePickerViewState.DAYS -> viewModel.switchToMonthsView()
                                DatePickerViewState.MONTHS -> viewModel.switchToYearsView()
                                else -> {}
                            }
                        }
                    )
                    
                    Row {
                        IconButton(onClick = { viewModel.navigatePrevious() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Previous", tint = themeColor)
                        }
                        IconButton(onClick = { viewModel.navigateNext() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next", tint = themeColor)
                        }
                    }
                }

                if (isSwitchable) {
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        DateSelectionMode.entries.forEachIndexed { index, m ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = DateSelectionMode.entries.size),
                                onClick = { 
                                    viewModel.setMode(m)
                                    onModeChanged(m)
                                },
                                selected = state.mode == m
                            ) {
                                Text(m.name.lowercase().capitalize(), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        },
        body = {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                when (state.viewState) {
                    DatePickerViewState.DAYS -> {
                        DaysView(
                            month = state.currentMonth,
                            selectedDates = state.selectedDates,
                            mode = state.mode,
                            themeColor = themeColor,
                            minDate = state.minDate,
                            maxDate = state.maxDate,
                            onDateClick = { viewModel.onDateClick(it) }
                        )
                    }
                    DatePickerViewState.MONTHS -> {
                        MonthsView(
                            year = state.viewingYear,
                            selectedDates = state.selectedDates,
                            granularity = state.granularity,
                            mode = state.mode,
                            themeColor = themeColor,
                            minDate = state.minDate,
                            maxDate = state.maxDate,
                            onMonthClick = { viewModel.onMonthClick(it) }
                        )
                    }
                    DatePickerViewState.YEARS -> {
                        YearsView(
                            baseYear = state.viewingYear,
                            selectedDates = state.selectedDates,
                            granularity = state.granularity,
                            mode = state.mode,
                            themeColor = themeColor,
                            minDate = state.minDate,
                            maxDate = state.maxDate,
                            onYearClick = { viewModel.onYearClick(it) }
                        )
                    }
                }
            }
        },
        actions = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = themeColor)
            }
            Button(
                onClick = { onConfirmed(state.selectedDates) },
                colors = ButtonDefaults.buttonColors(containerColor = themeColor)
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun DaysView(
    month: LocalDate,
    selectedDates: List<LocalDate>,
    mode: DateSelectionMode,
    themeColor: Color,
    minDate: LocalDate,
    maxDate: LocalDate,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = LocalDate(month.year, month.month, 1)
    val daysInMonth = month.month.length(isLeapYear(month.year))
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.ordinal) % 7

    val days = (0 until firstDayOfWeek).map { null } + (1..daysInMonth).map { LocalDate(month.year, month.month, it) }

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                Text(
                    it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize()
        ) {
            items(days) { date ->
                if (date != null) {
                    val isSelected = selectedDates.contains(date)
                    val isInRange = mode == DateSelectionMode.RANGE && selectedDates.size == 2 && 
                                    date > selectedDates.minOrNull()!! && date < selectedDates.maxOrNull()!!
                    val isEnabled = date >= minDate && date <= maxDate
                    
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected -> themeColor
                                    isInRange -> themeColor.copy(alpha = 0.2f)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable(enabled = isEnabled) { onDateClick(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = when {
                                isSelected -> Color.White
                                !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    Box(modifier = Modifier.aspectRatio(1f))
                }
            }
        }
    }
}

@Composable
private fun MonthsView(
    year: Int,
    selectedDates: List<LocalDate>,
    granularity: DateGranularity,
    mode: DateSelectionMode,
    themeColor: Color,
    minDate: LocalDate,
    maxDate: LocalDate,
    onMonthClick: (LocalDate) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()) {
        items(Month.entries) { month ->
            val date = LocalDate(year, month, 1)
            val isSelected = granularity == DateGranularity.MONTH && selectedDates.contains(date)
            
            // For months view, we enable it if any part of the month is within range
            val lastDayOfMonth = LocalDate(year, month, month.length(isLeapYear(year)))
            val isEnabled = lastDayOfMonth >= minDate && date <= maxDate

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(48.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(if (isSelected) themeColor else Color.Transparent)
                    .clickable(enabled = isEnabled) { onMonthClick(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    month.name.lowercase().take(3).capitalize(),
                    color = when {
                        isSelected -> Color.White
                        !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}

@Composable
private fun YearsView(
    baseYear: Int,
    selectedDates: List<LocalDate>,
    granularity: DateGranularity,
    mode: DateSelectionMode,
    themeColor: Color,
    minDate: LocalDate,
    maxDate: LocalDate,
    onYearClick: (LocalDate) -> Unit
) {
    val years = (baseYear - 10)..(baseYear + 10)
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()) {
        items(years.toList()) { year ->
            val date = LocalDate(year, Month.JANUARY, 1)
            val isSelected = granularity == DateGranularity.YEAR && selectedDates.contains(date)
            
            // For years view, we enable it if the year is within range
            val isEnabled = year >= minDate.year && year <= maxDate.year

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(48.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(if (isSelected) themeColor else Color.Transparent)
                    .clickable(enabled = isEnabled) { onYearClick(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    year.toString(),
                    color = when {
                        isSelected -> Color.White
                        !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
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

private fun String.capitalize(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
