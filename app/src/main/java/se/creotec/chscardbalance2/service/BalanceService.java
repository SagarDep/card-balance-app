// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.GlobalState;
import se.creotec.chscardbalance2.model.BackendResponse;
import se.creotec.chscardbalance2.model.CardData;

public class BalanceService extends AbstractBackendService<CardData> {

    public static final String LOG_TAG = BalanceService.class.getName();
    private final Type responseType = new TypeToken<BackendResponse<CardData>>() {}.getType();

    public BalanceService() {
        super(BalanceService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(LOG_TAG, "Received intent");
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(Constants.ACTION_UPDATE_CARD)) {
            try {
                GlobalState global = (GlobalState) getApplication();
                String cardNumber = global.getModel().getCardData().getCardNumber();
                BackendResponse<CardData> response = getBackendData(Constants.ENDPOINT_BALANCE, cardNumber);
                global.getModel().setCardLastTimeUpdated(System.currentTimeMillis());
                global.getModel().setCardData(response.getData());
                global.saveCardData();
                Log.i(LOG_TAG, "Got response: " + response.getData().toString());
            } catch (BackendFetchException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
    }

    @Override
    protected void validateVariable(String variable) throws BackendFetchException {
        if (variable.length() != Constants.CARD_NUMBER_LENGTH) {
            throw new BackendFetchException("Card number has incorrect length");
        }
        // Make sure card number only contains numbers and has no blank spaces
        if (!variable.matches("[0-9]+")) {
            throw new BackendFetchException("Card number contains invalid characters");
        }
    }

    @Override
    protected Type getResponseType() {
        return responseType;
    }


}
