package com.example.android.politicalpreparedness.election

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ElectionsFragment : BaseFragment<FragmentElectionBinding, ElectionsViewModel>() {
    override val viewModel: ElectionsViewModel by viewModels()
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentElectionBinding.inflate(inflater)

    override fun initViews() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setUpRecUpComingElections()
        setUpRecSavedElections()
    }

    private fun setUpRecUpComingElections() {
        val upcomingElectionsAdapter = ElectionListAdapter(ElectionListener { election -> findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election.id,
                    election.division,
                    election
                )
            )
        })
        binding.recUpcomingElection.adapter = upcomingElectionsAdapter
        viewModel.upcomingElections.observe(viewLifecycleOwner) {
            upcomingElectionsAdapter.submitList(it)
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun setUpRecSavedElections() {
        val electionsAdapterSaved = ElectionListAdapter(ElectionListener { election ->
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election.id,
                    election.division,
                    election
                )
            )
        })
        binding.recSavedElection.adapter = electionsAdapterSaved
        viewModel.savedElections.observe(viewLifecycleOwner) {
            electionsAdapterSaved.submitList(it)
        }
    }


}