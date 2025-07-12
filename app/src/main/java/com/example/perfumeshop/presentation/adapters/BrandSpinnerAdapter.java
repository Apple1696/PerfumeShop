package com.example.perfumeshop.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.perfumeshop.data.models.entities.Brand;

import java.util.List;

public class BrandSpinnerAdapter extends ArrayAdapter<Brand> {
    private LayoutInflater inflater;

    public BrandSpinnerAdapter(@NonNull Context context, @NonNull List<Brand> brands) {
        super(context, android.R.layout.simple_spinner_item, brands);
        inflater = LayoutInflater.from(context);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        
        TextView textView = convertView.findViewById(android.R.id.text1);
        Brand brand = getItem(position);
        textView.setText(brand != null ? brand.getBrandName() : "");
        
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        
        TextView textView = convertView.findViewById(android.R.id.text1);
        Brand brand = getItem(position);
        textView.setText(brand != null ? brand.getBrandName() : "");
        
        return convertView;
    }
}
