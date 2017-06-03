package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;

public class QuoteCreateActivity extends BaseCreateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean validateForm() {
        // TODO: 02/06/2017
        return false;
    }

    @Override
    protected Intent getResultIntent() {
        return new Intent();
    }
}
