package com.example.reservesig_pid.filters;

import android.widget.Filter;

import com.example.reservesig_pid.adapters.AdapterCategory;
import com.example.reservesig_pid.adapters.AdapterPdfAdmin;
import com.example.reservesig_pid.models.ModelCategory;
import com.example.reservesig_pid.models.ModelPdf;

import java.util.ArrayList;

public class FilterPdfAdmin extends Filter {
    //array dads lequel on va chercher

    ArrayList<ModelPdf> filterList;
    //Adapter ou le filtre a besoin d'être implémenter
    AdapterPdfAdmin adapterPdfAdmin;

    public FilterPdfAdmin(ArrayList<ModelPdf> filterList, AdapterPdfAdmin adapterPdfAdmin) {
        this.filterList = filterList;
        this.adapterPdfAdmin = adapterPdfAdmin;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //doit pas être null ni vide

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
        //applique les changemnts
        adapterPdfAdmin.pdfArrayList = (ArrayList<ModelPdf>)results.values;

        //notifie les changments
        adapterPdfAdmin.notifyDataSetChanged();

    }
}
