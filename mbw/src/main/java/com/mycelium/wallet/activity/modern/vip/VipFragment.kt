package com.mycelium.wallet.activity.modern.vip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.mycelium.wallet.databinding.FragmentVipBinding

class VipFragment : Fragment() {

    private lateinit var binding: FragmentVipBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentVipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            vipCodeInput.doOnTextChanged { text, _, _, _ ->
                applyButton.isVisible = !text.isNullOrEmpty()
            }
            applyButton.setOnClickListener {
                errorText.text = vipCodeInput.text.toString()
            }
        }
    }
}