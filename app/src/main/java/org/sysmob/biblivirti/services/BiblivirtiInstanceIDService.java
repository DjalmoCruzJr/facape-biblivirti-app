package org.sysmob.biblivirti.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by djalmocruzjr on 23/01/2017.
 */

public class BiblivirtiInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = BiblivirtiInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Falta imlementar o metodo de envio do registration_id do app para o servidor
    }
}