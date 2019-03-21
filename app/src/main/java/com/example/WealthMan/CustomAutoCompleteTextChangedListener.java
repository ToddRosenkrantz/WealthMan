package com.example.WealthMan;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
    public class CustomAutoCompleteTextChangedListener implements TextWatcher {

        public static final String TAG = "CustomTextListener";
        Context context;

        public CustomAutoCompleteTextChangedListener(Context context){
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

                HomeActivity homeActivity = ((HomeActivity) context);

                // update the adapater
                homeActivity.myAdapter.notifyDataSetChanged();

                // get suggestions from the database
                NameSymbol[] myObjs = homeActivity.db.read(userInput.toString());

                // update the adapter
                homeActivity.myAdapter = new AutocompleteCustomArrayAdapter(homeActivity, R.layout.list_view_row_item, myObjs);

                homeActivity.myAutoComplete.setAdapter(homeActivity.myAdapter);

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }
