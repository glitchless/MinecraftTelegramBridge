package ru.glitchless.telegrambridge.core.telegramapi.delegate;

import java.io.*;

public class TelegramOffsetDataHelper {
    private static Long offset = -1L;
    private static File offsetFile = new File("telegrambridgesoffset.dat");

    public static Long getOffset() throws IOException {
        if (offset < 0) {
            if (!offsetFile.exists()) {
                offsetFile.createNewFile();
            }

            String offsetText = readFile(offsetFile);
            if (offsetText.length() == 0) {
                offset = 0L;
            } else {
                offset = Long.valueOf(offsetText.replaceAll("[\\n\\t ]", "").trim());
            }
        }
        return offset;
    }

    public static void setIfLargerOffset(Long localOffset) throws IOException {
        if (localOffset <= offset) {
            return;
        }

        offset = localOffset;
        if (!offsetFile.exists()) {
            offsetFile.getParentFile().mkdirs();
            offsetFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(offsetFile);
        fileWriter.write(String.valueOf(offset));
        fileWriter.close();
    }

    private static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = new FileInputStream(file);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
        } catch (Exception ex) {
            offsetFile.delete();
            return String.valueOf(0);
        }
        return sb.toString();
    }

}
