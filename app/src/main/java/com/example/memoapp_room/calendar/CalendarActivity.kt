package com.example.memoapp_room.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.memoapp_room.R
import com.example.memoapp_room.databinding.ActivityCalendarBinding
import com.example.memoapp_room.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    companion object {
        val TAG = CalendarActivity::class.simpleName
    }

    private lateinit var binding: ActivityCalendarBinding
    private var selectedDate = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)

        // onBackPressedDispatcher 콜백 추가
        onBackPressedDispatcher.addCallback(this, callback)

        setLayout()
    }

    private fun setLayout() {
        class DayViewContainer(view: View) : ViewContainer(view) {
            // 각 셀의 뷰 홀더 역할을 하는 뷰 컨테이너
            val dayBinding = CalendarDayLayoutBinding.bind(view)

            lateinit var day: WeekDay

            init {
                view.setOnClickListener {
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.calendarView.notifyDateChanged(day.date)
                        oldDate?.let { binding.calendarView.notifyDateChanged(it) }
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                val key = day.date.toString().replace("-", "")
                Log.d(TAG, "key : $key")

                val textColorRes = if (day.date == selectedDate) R.color.white else R.color.black
                val backgroundColorRes = if (day.date == selectedDate) R.color.c_FD7070 else R.color.c_F3F3F3
                with(dayBinding) {
                    calendarDateText.text = dateFormatter.format(day.date)
                    calendarDayText.text = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    calendarDateText.setTextColor(ContextCompat.getColor(this@CalendarActivity, textColorRes))
                    calendarDayText.setTextColor(ContextCompat.getColor(this@CalendarActivity, textColorRes))
                    calendarDateView.setBackgroundColor(ContextCompat.getColor(this@CalendarActivity, backgroundColorRes))
                }

                binding.monthTextView.text = setYearMonth(day)
            }
        }

        binding.calendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: WeekDay) {
                container.bind(data)
            }
        }

        val currentMonth = YearMonth.now()
        binding.calendarView.setup(
            currentMonth.minusMonths(5).atStartOfMonth(),
            currentMonth.plusMonths(5).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )

        // 오늘 날짜 지정
        binding.calendarView.scrollToDate(LocalDate.now())

        // 스크롤 시 년/월 글자 수정
        binding.calendarView.weekScrollListener = {
            binding.monthTextView.text = setYearMonth(it.days.last())
        }

        // onBackPressed 호출
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun setYearMonth(week: WeekDay): String {
        val month = week.date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val year = week.date.year.toString()

        return "$month $year"
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!this@CalendarActivity.isFinishing) {
                this@CalendarActivity.finish()
            }
        }
    }
}