package com.tiun.composite.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tiun.composite.R
import com.tiun.composite.databinding.FragmentChooseLevelBinding
import com.tiun.composite.domain.entity.Level

class ChooseLevelFragment : Fragment() {
    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLevelEasy.setOnClickListener {
            launchGameFragment(Level.EASY)
        }
        binding.buttonLevelNormal.setOnClickListener {
            launchGameFragment(Level.NORMAL)
        }
        binding.buttonLevelHard.setOnClickListener {
            launchGameFragment(Level.HARD)
        }
        binding.buttonLevelTest.setOnClickListener {
            launchGameFragment(Level.TEST)
        }
    }

    private fun launchGameFragment(level: Level) {
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
