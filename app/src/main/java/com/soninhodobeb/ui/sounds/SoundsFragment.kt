package com.soninhodobeb.ui.sounds

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.soninhodobeb.R
import com.soninhodobeb.databinding.FragmentSoundsBinding
import com.soninhodobeb.views.SelectorButton
import com.soninhodobeb.views.SoundButton
import java.util.concurrent.TimeUnit

class SoundsFragment : Fragment() {
    private var isSelectorExpanded: Boolean = false
    private var selectedGroup = 0
    private var isTimerShowing: Boolean = false
    private var isDialogShowing = false
    private var _binding: FragmentSoundsBinding? = null
    private val binding get() = _binding!!

    private var isControllerExpanded: Boolean = false

    private var countDownTimer: CountDownTimer? = null

    private lateinit var viewModel: SoundsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[SoundsViewModel::class.java]

        _binding = FragmentSoundsBinding.inflate(inflater, container, false)
        val groups = resources.getStringArray(R.array.sound_groups)
        groups.forEachIndexed { i, it ->
            val selectorBtn = SelectorButton(requireContext())
            val groupTitleFormatted = it.replace(" ", "_")
                .lowercase()
            val drawableId = resources.getIdentifier(
                groupTitleFormatted,
                "drawable",
                requireContext().packageName
            )
            selectorBtn.setText(it)
            selectorBtn.setImage(ResourcesCompat.getDrawable(resources, drawableId, null))

            selectorBtn.findViewById<ViewGroup>(R.id.root).setOnClickListener {
                if (i != selectedGroup) {
                    selectedGroup = i
                    selectSoundGroup(selectedGroup)
                    binding.soundGroupSelector.performClick()
                } else {
                    binding.soundGroupSelector.performClick()
                }
            }

            if (i != 0) {
                selectorBtn.visibility = View.GONE
            }
            binding.selectorOptions.addView(selectorBtn)
        }

        selectSoundGroup(selectedGroup)

        binding.soundGroupSelector.setOnClickListener {
            toggleSelector()
        }

        binding.btnTimer.setOnClickListener {
            if (isTimerShowing) {
                binding.llTimerOptions.visibility = View.GONE
                binding.btnExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                binding.llTimerOptions.visibility = View.VISIBLE
                binding.btnExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }

            isTimerShowing = !isTimerShowing
        }

        binding.customTimer.setOnClickListener {
            if (viewModel.isTimerEnabled.value != true) {
                customTimer()
            } else {
                customTimer(true)
            }
        }

        binding.btnTenMin.setOnClickListener {
            setTimer((TimeUnit.MINUTES.toMillis(10)))
        }

        binding.btnThirtyMin.setOnClickListener {
            setTimer((TimeUnit.MINUTES.toMillis(30)))
        }

        binding.btnOneHour.setOnClickListener {
            setTimer((TimeUnit.MINUTES.toMillis(60)))
        }

        val observer = Observer<Long> {
            if (it > 0) {
                val hours: Long = it / 3600000
                val minutes: Long = (it - hours * 3600000) / 60000
                val seconds: Long = ((it - hours * 3600000) - minutes * 60000) / 1000

                val hoursString = if (hours > 9) hours.toString() else "0$hours"
                val minString = if (minutes > 9) minutes.toString() else "0$minutes"
                val secondString = if (seconds > 9) seconds.toString() else "0$seconds"
                binding.tvTimer.text = getString(
                    R.string.timer_template,
                    getString(R.string.hour_template, hoursString, minString, secondString)
                )
                binding.tvTimer.visibility = View.VISIBLE
            } else {
                binding.tvTimer.visibility = View.GONE
            }
        }

        viewModel.timerInMillis.observe(viewLifecycleOwner, observer)

        binding.btnPlayer.setOnClickListener {
            viewModel.isPlaying.value =
                if (viewModel.isPlaying.value != null) !viewModel.isPlaying.value!! else false
        }

        val playerObserver = Observer<Boolean> {
            if (it) {
                startPlaying()
                binding.btnPlayer.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                pausePlaying()
                binding.btnPlayer.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }

        viewModel.isPlaying.observe(viewLifecycleOwner, playerObserver)

        binding.btnExpand.setOnClickListener {
            if (isControllerExpanded or isSelectorExpanded) {
                binding.llPlayerControls.visibility = View.GONE
                binding.llTimerOptions.visibility = View.GONE

                binding.btnExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                binding.llPlayerControls.visibility = View.VISIBLE
                binding.btnExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }

            if (isSelectorExpanded) {
                binding.btnTimer.performClick()
            }

            isControllerExpanded = !isControllerExpanded
        }

        return binding.root
    }

    private fun startPlaying() {
        val map = viewModel.mediaPlayerMap.value ?: run {
            return
        }

        for ((_, player) in map) {
            player.start()
        }

        val timer = (viewModel.timerInMillis.value ?: 0 > 0)
        if (timer) {
            val time = viewModel.timerInMillis.value ?: 0
            countDownTimer = object : CountDownTimer(time, 1000) {
                override fun onTick(p0: Long) {
                    viewModel.timerInMillis.value = p0
                }

                override fun onFinish() {
                    viewModel.isTimerEnabled.value = false
                    viewModel.timerInMillis.value = 0
                }

            }
            countDownTimer?.start()
            viewModel.isTimerEnabled.value = true
        }
    }

    private fun pausePlaying() {
        val map = viewModel.mediaPlayerMap.value ?: run {
            return
        }

        for ((_, player) in map) {
            player.pause()
        }

        val timer = (viewModel.timerInMillis.value ?: 0 > 0)
        if (timer) {
            countDownTimer?.cancel()
        }

    }

    private fun setTimer(timeInMillis: Long) {
        if (viewModel.isTimerEnabled.value != true) {
            viewModel.timerInMillis.value = (timeInMillis)
            val timer = countDownTimer ?: kotlin.run {
                return
            }

            timer.cancel()
        } else {
            showTimerChangeConfirmation(timeInMillis)
        }
    }

    private fun showTimerChangeConfirmation(timeInMillis: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.are_you_sure)
            .setMessage(R.string.timer_change_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.isTimerEnabled.value = false
                setTimer(timeInMillis)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun customTimer(isSet: Boolean = false) {
        if (!isDialogShowing) {
            isDialogShowing = true
            val dialog = MaterialTimePicker.Builder()
                .setTimeFormat(CLOCK_24H)
                .setTitleText(getString(R.string.timer_select))
                .build()

            dialog.isCancelable = true
            dialog.addOnPositiveButtonClickListener {
                val hourInMin = dialog.hour * 60
                val minutes = dialog.minute
                if (!isSet) {
                    viewModel.timerInMillis.value = (hourInMin + minutes).toLong() * 60000
                } else {
                    showTimerChangeConfirmation((hourInMin + minutes).toLong() * 60000)
                }
            }

            dialog.addOnDismissListener { isDialogShowing = false }

            dialog.show(parentFragmentManager, getString(R.string.TIME_PICKER_TAG))
        }
    }

    override fun onPause() {
        super.onPause()
        isTimerShowing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selectSoundGroup(groupId: Int) {
        binding.flexboxSounds.removeAllViews()

        var itemArray = arrayOf<String>()

        when (groupId) {
            0 -> itemArray = resources.getStringArray(R.array.sounds_rain_water)
            1 -> itemArray = resources.getStringArray(R.array.sounds_nature)
        }

        itemArray.forEach { soundTitle ->
            val soundTitleFormatted = soundTitle.replace(" ", "_")
                .lowercase()
            val drawableId = resources.getIdentifier(
                soundTitleFormatted,
                "drawable",
                requireContext().packageName
            )
            val soundButton = SoundButton(requireContext())
            soundButton.setText(soundTitle)
            soundButton.setImage(
                ResourcesCompat.getDrawable(
                    resources,
                    drawableId,
                    null
                )
            )
            soundButton.setOnClickListener {
                val soundId = resources.getIdentifier(
                    soundTitleFormatted,
                    "raw",
                    requireContext().packageName
                )

                if (viewModel.nowPlayingList.value?.contains(soundTitleFormatted) == false) {
                    val mediaPlayer = MediaPlayer.create(requireContext(), soundId)
                    viewModel.mediaPlayerMap.value?.put(soundTitleFormatted, mediaPlayer)
                    viewModel.nowPlayingList.value?.add(soundTitleFormatted)
                    binding.btnPlayer.performClick()
                }

            }

            binding.flexboxSounds.addView(soundButton)
        }
    }

    private fun toggleSelector() {
        val visibility = if (!isSelectorExpanded) View.VISIBLE else View.GONE

        binding.selectorOptions.children.forEachIndexed { index, view ->
            if (index != selectedGroup) {
                view.visibility = visibility
            } else {
                view.visibility = View.VISIBLE
            }
        }

        isSelectorExpanded = !isSelectorExpanded
    }
}