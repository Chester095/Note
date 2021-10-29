package com.geekbrains.note.io;

import android.content.Context;
import android.util.Log;

import com.geekbrains.note.domain.App;
import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.ui.NotesListFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SaveFile extends NotesListFragment {

    static final String LOG_TAG = "@@@";
    static final String FILENAME = "file.txt";


    /*** Запись в файл
     *
     * @param data Данные для записи
     * @param ctx Контекст
     * @param add Определяет добавление записи в файл или полная перезапить
     *            (необходимо в случае удаления одной из записей)
     */
    public static boolean writeToFile(String data, Context ctx, Boolean add) {
        FileOutputStream fou;
        try {
            if (add == true) {
                fou = ctx.openFileOutput(FILENAME, Context.MODE_APPEND);
            } else {
                fou = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fou, StandardCharsets.UTF_8);
//            Log.d(LOG_TAG, "writeToFile " + FILENAME + "  " + data);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
    }

    public String updateFile() {
        String temp = "";
        List<NoteEntity> data = notesRepo.getNotes();
        for (NoteEntity noteEntity : data) {
            temp += new IoAdapter().saveToFile(noteEntity.getId(), noteEntity.getTitle(), noteEntity.getDescription());
        }
        Log.d(LOG_TAG, temp);
        return temp;
    }

    /***  Чтение из файла
     *
     * @param ctx контекст
     *            происходит построчное считывание данных
     * @return
     */
    public static String readFromFile(Context ctx) {

        String ret = "";
        try {
            InputStream inputStream;
            inputStream = ctx.openFileInput(FILENAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString + "\r\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
//            Log.d(LOG_TAG, "readFile " + FILENAME + "  " + ret);
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

/*    public static void readFromFile(Context ctx) {

        FileInputStream fos;
        try {
            fos = ctx.openFileInput("file.txt");

            ObjectInputStream oos = new ObjectInputStream(fos);
            String str = (String) oos.readObject();
            Log.d("@@@", "readFile oos  " + str);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

/*    void writeFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
