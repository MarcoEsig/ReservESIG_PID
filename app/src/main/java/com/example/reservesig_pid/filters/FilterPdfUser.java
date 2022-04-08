package com.example.reservesig_pid.filters;

import android.widget.Filter;

import com.example.reservesig_pid.adapters.AdapterPdfUser;
import com.example.reservesig_pid.models.ModelPdf;

import java.util.ArrayList;

public class FilterPdfUser extends Filter {

    ArrayList<ModelPdf> filterList;
    AdapterPdfUser adapterPdfUser;

    public FilterPdfUser(ArrayList<ModelPdf> filterList, AdapterPdfUser adapterPdfUser) {
        this.filterList = filterList;
        this.adapterPdfUser = adapterPdfUser;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0){

            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelPdf> filteredModels = new ArrayList<>();

            for (int i =0; i<filterList.size(); i++) {

                //validation
                if (filterList.get(i).getTitle().toUpperCase().contains(constraint)){
                    //ajout dans la liste filtré

                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {

        //applique filtre
        adapterPdfUser.pdfArrayList = (ArrayList<ModelPdf>) results.values;

        //notifie les changements
        adapterPdfUser.notifyDataSetChanged();

    }
}
