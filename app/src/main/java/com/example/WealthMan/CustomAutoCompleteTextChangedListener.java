package com.example.WealthMan;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
    public class CustomAutoCompleteTextChangedListener implements TextWatcher {

        public static final String TAG = "CustomTextListener";
        private HomeFragment homeFragment;
        Context context;

        public CustomAutoCompleteTextChangedListener(HomeFragment fragment, Context context){
            this.homeFragment = fragment;
            this.context = context;
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence userInput, int start, int before, int count) {

            try{

                // if you want to see in the logcat what the user types
                Log.e(TAG, "User input: " + userInput);

                // update the adapater
                homeFragment.myAdapter.notifyDataSetChanged();

                // get suggestions from the database
                NameSymbol[] myObjs = homeFragment.db.read(userInput.toString());

                // update the adapter
                homeFragment.myAdapter = new AutocompleteCustomArrayAdapter(context, R.layout.list_view_row_item, myObjs);

                homeFragment.myAutoComplete.setAdapter(homeFragment.myAdapter);

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }
