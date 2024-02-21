package com.minibyte.bo.dto.dataprovider;

import com.minibyte.common.exception.MBBizException;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: AntHubTC
 * @date: 2023/1/12
 * @description:
 */
@Data
public class DataAnalyseContext implements Serializable  {
    private Map<String, Object> storeMap = new HashMap<>();

    private transient File file;

    public static DataAnalyseContext readData(File file) {
        DataAnalyseContext dataAnalyseContext = new DataAnalyseContext();
        dataAnalyseContext.setFile(file);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    throw new MBBizException("创建文件失败!");
                }
                return dataAnalyseContext;
            } catch (IOException e) {
                e.printStackTrace();
                throw new MBBizException(e.getMessage());
            }
        }
        if (file.length() == 0) {
            return dataAnalyseContext;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            dataAnalyseContext = (DataAnalyseContext) ois.readObject();
            dataAnalyseContext.setFile(file);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new MBBizException(e.getMessage());
        }
        return dataAnalyseContext;
    }

    public void writeData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.file))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MBBizException(e.getMessage());
        }
    }
}
