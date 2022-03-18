package com.example.reservesig_pid;

import android.widget.Adapter;
import android.widget.Filter;

import com.google.android.material.badge.BadgeDrawable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FilterCategory extends Filter {
    //array dads lequel on va chercher

    ArrayList<ModelCategory> filterList;
    //Adapter ou le filtre a besoin d'être implémenter
    AdapterCategory adapterCategory;

    public FilterCategory(ArrayList<ModelCategory> filterList, AdapterCategory adapterCategory) {
        this.filterList = filterList;
        this.adapterCategory = adapterCategory;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //doit pas être null ni vide

        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelCategory> filteredModels = new ArrayList<>();

            for (int i =0; i<filterList.size(); i++) {

                //validation
                if (filterList.get(i).getCategory().toUpperCase().contains(constraint)){
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
        adapterCategory.categoryArrayList = (ArrayList<ModelCategory>)results.values;

        //notifie les changments
        adapterCategory.notifyDataSetChanged();

    }
}
