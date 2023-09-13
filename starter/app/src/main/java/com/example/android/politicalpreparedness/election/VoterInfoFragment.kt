package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoterInfoFragment : BaseFragment<FragmentVoterInfoBinding, VoterInfoViewModel>() {
    override val viewModel: VoterInfoViewModel by viewModels()
    private val args: VoterInfoFragmentArgs by navArgs()


    override fun initViews() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.setElectionId(args.argElectionId)
        viewModel.election=args.election
        viewModel.setLocation("${args.election.division.country},${args.election.division.state}")
        viewModel.getVoterInfoResponse("${args.election.division.country},${args.election.division.state}", args.argElectionId)
        viewModel.checkIfElectionInLocal(args.argElectionId)
        viewModel.savedElection.observe(viewLifecycleOwner) { election ->
            if (election == args.election) {
                binding.followButton.text = "UnFollow Election"
                viewModel.electionIsInLocal = true
            } else {
                binding.followButton.text = "Follow Election"
                viewModel.electionIsInLocal = false
            }
        }
        viewModel.openUrl.observe(viewLifecycleOwner) { url ->
            openUrlUsingIntent(url)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentVoterInfoBinding.inflate(inflater)

    private fun openUrlUsingIntent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}