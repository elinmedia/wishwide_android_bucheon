package com.media.mobile.elin.wishwidemobile;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileFetcher {
    private static final String TAG = "FileFetcher";
    private String mStoragePath = "";
    private final static String strAppPath = Environment.getExternalStorageDirectory().getPath() + "/Wishwide/game/";
//    private final static String strAppDatasetPath = Environment.getExternalStorageDirectory().getPath() + "/Wishwide/game/dataset/";
//    private final static String strAppCharacterPath = Environment.getExternalStorageDirectory().getPath() + "/Wishwide/game/character/";


    public FileFetcher(String storagePath) {
        mStoragePath = storagePath + "/";
    }

    //Cloud에서 다운로드한 파일을 콘텐츠 폴더에 파일명을 "콘텐츠명.확장자"로 저장
    public void downloadDataSetFile(
            String dataSetUrl,
            String dataSetFileName,
            int dataSetFileSize) {

        new DataSetFileDownloadTask(
                dataSetUrl,
                dataSetFileName,
                dataSetFileSize).execute();
    }

    private class DataSetFileDownloadTask extends AsyncTask<String, Void, String> {
        private final String mDataSetUrl;
        private final String mDataSetFileName;
        private final int mDataSetFileSize;

        public DataSetFileDownloadTask(
                String dataSetUrl,
                String dataSetFileName,
                int dataSetFileSize) {
            mDataSetUrl = dataSetUrl;
            mDataSetFileName = dataSetFileName;
            mDataSetFileSize = dataSetFileSize;
        }

        @Override
        protected String doInBackground(String... params) {

//            Log.i(TAG, "콘텐츠 아이템 확인 : " + mGameCharacterFileVO);
            File contentsDir = new File(mStoragePath);
            int readByte = 0;

            //콘텐츠 관리 폴더 존재 확인, 없으면 생성
            boolean isContentExist = checkDirectory(contentsDir);

//            Log.d(TAG, "폴더 존재 확인: " + isContentExist);
            if (!isContentExist) {
                makeDirectory(contentsDir);
            } else {
//                removeFileAll();
            }

            HttpURLConnection connection = null;
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                //인터넷 연결
                URL url = new URL(mDataSetUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                inputStream = new BufferedInputStream(connection.getInputStream());

                outputStream = new FileOutputStream(mStoragePath + mDataSetFileName);
                byte[] buffer = new byte[connection.getContentLength()];

                while ((readByte = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readByte);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }

                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (connection != null) {
                        connection.disconnect();
                    }

                } catch (IOException e) {
                }
            }
            return mStoragePath + mDataSetFileName;
        }

        @Override
        protected void onPostExecute(String s) {

            while (!checkCompleteFile(s, mDataSetFileSize)) {
            }
        }
    }

    public List<String> getGameCharacterFilePaths() {
        File[] files = new File(mStoragePath).listFiles();

        List<String> filePaths = new ArrayList();

        for (int i = 0; i < files.length; i++) {
            filePaths.add(files[i].getPath());
        }

        return filePaths;
    }

    //스케줄 파일 가져오기
    public InputStream readFile(String path) {
        Log.d(TAG, "최신 스케줄 경로: " + path);
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path);

            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //파일 디렉토리 존재 여부 체크
    public boolean checkDirectory(File dir) {
        return dir.exists();
    }

    //파일 디렉토리 생성
    public boolean makeDirectory(File dir) {
        return dir.mkdirs();
    }

    public boolean checkGameSettingFile() {
        int isFileCnt = 0;
        File[] files = new File(mStoragePath).listFiles();

        for (File file : files) {
            Log.d(TAG, "게임 설정 관련 파일 이름 확인: " + file.getName());
            if (file.getName().contains("xmlMembershipCustomer.xml"))
                isFileCnt++;
            else if (file.getName().contains(".dat"))
                isFileCnt++;
            else if (file.getName().contains("xmlGameSetting.xml"))
                isFileCnt++;
        }

        if (isFileCnt == 3) {
            return true;
        } else {
            return false;
        }
    }

    public void removeFileAll() {
        File[] files = new File(mStoragePath).listFiles();

        if (files == null || files.length < 1) {
            return;
        }

        for (File file : files) {
            //폴더일 경우 폴더 내 파일들 먼저 제거 후 폴더 제거
            if (file.isDirectory()) {
//                File[] scheduleFiles = file.listFiles();
//
//                if (scheduleFiles.length > 0) {
//                    for (int i = 0; i < scheduleFiles.length; i++) {
//                        scheduleFiles[i].delete();
//                    }
//                }
//
//                file.delete();
            }
            //파일일 경우 파일 제거
            else if (file.isFile()) {
                file.delete();
            }
        }

        files = new File(strAppPath).listFiles();

        if (files == null || files.length < 1) {
            return;
        }

        for (File file : files) {
            //폴더일 경우 폴더 내 파일들 먼저 제거 후 폴더 제거
            if (file.isDirectory()) {
                File[] scheduleFiles = file.listFiles();

                if (scheduleFiles.length > 0) {
                    for (int i = 0; i < scheduleFiles.length; i++) {
                        scheduleFiles[i].delete();
                    }
                }

                file.delete();
            }
            //파일일 경우 파일 제거
            else if (file.isFile()) {
                file.delete();
            }
        }
    }

    public boolean checkCompleteFile(String filePath, int fileSize) {
        File file = new File(filePath);

        if (file.exists()) {
            //콘텐츠 파일 있음
            //기기에 저장된 콘텐츠 파일과 클라우드에 저장된 콘텐츠 사이즈가 같은지 확인
            boolean isSameFile = fileSize == file.length();

            Log.i(TAG, fileSize + " 파일 사이즈 확인 " + file.length());

            return isSameFile;
        }

        return false;
    }
}
