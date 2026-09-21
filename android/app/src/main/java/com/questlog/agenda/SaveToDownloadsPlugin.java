package com.questlog.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// Guarda un archivo de texto directamente en la carpeta pública de Descargas
// del dispositivo usando MediaStore (API 29+), para que el respaldo aparezca
// como una descarga normal en vez de quedar dentro de la carpeta privada de
// la app. @capacitor/filesystem no ofrece esto porque su enum Directory no
// incluye la carpeta pública de Descargas.
@CapacitorPlugin(name = "SaveToDownloads")
public class SaveToDownloadsPlugin extends Plugin {

    @PluginMethod
    public void saveText(PluginCall call) {
        String filename = call.getString("filename");
        String content = call.getString("content");
        String mimeType = call.getString("mimeType", "application/json");

        if (filename == null || content == null) {
            call.reject("filename y content son requeridos");
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            call.reject("Requiere Android 10 o superior");
            return;
        }

        Context context = getContext();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        Uri item = null;
        try {
            item = context.getContentResolver().insert(collection, values);
            if (item == null) {
                call.reject("No se pudo crear el archivo en Descargas");
                return;
            }
            OutputStream out = context.getContentResolver().openOutputStream(item);
            if (out == null) {
                call.reject("No se pudo abrir el archivo para escribir");
                return;
            }
            try {
                out.write(content.getBytes(StandardCharsets.UTF_8));
            } finally {
                out.close();
            }
            JSObject ret = new JSObject();
            ret.put("uri", item.toString());
            call.resolve(ret);
        } catch (Exception e) {
            if (item != null) {
                try {
                    context.getContentResolver().delete(item, null, null);
                } catch (Exception ignored) {}
            }
            call.reject("Error al guardar en Descargas: " + e.getMessage(), e);
        }
    }
}
