package app.simple.inure.ui.viewers

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import app.simple.inure.R
import app.simple.inure.adapters.details.AdapterFeatures
import app.simple.inure.decorations.views.CustomRecyclerView
import app.simple.inure.decorations.views.TypeFaceTextView
import app.simple.inure.extension.fragments.ScopedFragment
import app.simple.inure.viewmodels.factory.ApplicationInfoFactory
import app.simple.inure.viewmodels.panels.ApkDataViewModel

class Features : ScopedFragment() {

    private lateinit var recyclerView: CustomRecyclerView
    private lateinit var total: TypeFaceTextView
    private lateinit var componentsViewModel: ApkDataViewModel
    private lateinit var applicationInfoFactory: ApplicationInfoFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_features, container, false)

        recyclerView = view.findViewById(R.id.features_recycler_view)
        total = view.findViewById(R.id.total)
        applicationInfo = requireArguments().getParcelable<ApplicationInfo>("application_info")!!
        applicationInfoFactory = ApplicationInfoFactory(requireActivity().application, applicationInfo)
        componentsViewModel = ViewModelProvider(this, applicationInfoFactory).get(ApkDataViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startPostponedEnterTransition()

        componentsViewModel.getFeatures().observe(viewLifecycleOwner, {
            recyclerView.adapter = AdapterFeatures(it)
            total.text = getString(R.string.total, it.size)
        })
    }

    companion object {
        fun newInstance(applicationInfo: ApplicationInfo): Features {
            val args = Bundle()
            args.putParcelable("application_info", applicationInfo)
            val fragment = Features()
            fragment.arguments = args
            return fragment
        }
    }
}