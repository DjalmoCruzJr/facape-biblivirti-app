package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.model.Apresentacao;
import org.sysmob.biblivirti.model.Conteudo;
import org.sysmob.biblivirti.model.Exercicio;
import org.sysmob.biblivirti.model.Formula;
import org.sysmob.biblivirti.model.Jogo;
import org.sysmob.biblivirti.model.Livro;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.model.Simulado;
import org.sysmob.biblivirti.model.Video;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by djalmocruzjr on 24/01/2017.
 */

public abstract class BiblivirtiUtils {

    public static boolean isNetworkConnected() {
        Context context = BiblivirtiApplication.getInstance().getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static String getMimeType(String type) {
        String mimeType = "";
        switch (type) {
            case "image/jpeg":
            case "image/jpg":
                mimeType = "jpeg";
                break;
            case "image/png":
                mimeType = "png";
                break;
            default:
                mimeType = "jpeg";
        }
        return mimeType;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static String encondImage(Bitmap bitmap, String mimeType) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT) + "." + getMimeType(mimeType);
    }

    public static Material createMaterialByTipo(ETipoMaterial tipoMaterial) {
        Material material = null;

        if (tipoMaterial == ETipoMaterial.APRESENTACAO) {

            material = new Apresentacao();
        } else if (tipoMaterial == ETipoMaterial.EXERCICIO) {
            material = new Exercicio();
        } else if (tipoMaterial == ETipoMaterial.FORMULA) {
            material = new Formula();
        } else if (tipoMaterial == ETipoMaterial.JOGO) {
            material = new Jogo();
        } else if (tipoMaterial == ETipoMaterial.LIVRO) {
            material = new Livro();
        } else if (tipoMaterial == ETipoMaterial.SIMULADO) {
            material = new Simulado();
        } else if (tipoMaterial == ETipoMaterial.VIDEO) {
            material = new Video();
        }

        return material;
    }

}
