package com.nvidia.display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import com.sun.jna.Platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ShellService {

//    @Value("${micronaut.server.port}")
//    private Integer myServerPort;

    // @Autowired
    // private TrainModelRepository trainModelRepository;

    private static HashMap<String, Process> processHashMap = new HashMap<>();

    public String getGPU() throws IOException {
        Process process = null;
        try {
            if (Platform.isWindows()) {
                process = Runtime.getRuntime().exec("nvidia-smi.exe");
            } else if (Platform.isLinux()) {
                String[] shell = {"/bin/bash", "-c", "nvidia-smi"};
                process = Runtime.getRuntime().exec(shell);
            }

            process.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
           // throw new Exception("显卡不存在或获取显卡信息失败");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuffer stringBuffer = new StringBuffer();
        String line = "";
        while (null != (line = reader.readLine())) {
            stringBuffer.append(line + "\n");
        }

        return stringBuffer.toString();
    }
}
