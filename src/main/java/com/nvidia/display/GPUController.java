package com.nvidia.display;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class GPUController {

    @Autowired
    ShellService shellService;

    @ResponseBody
    @GetMapping(value = "/get/gpu")
    public String getGPU() throws IOException {
        List<GPUInfo> gpuInfoList = getGpuInfos();
        // return ApiResponse.responseSuccess(gpuInfoList);
        return gpuInfoList.size()+"";
    }


    @GetMapping(value = "/detail")
    public String getGPUDetail(Model model) throws IOException {
        model.addAttribute("hello","hello welcome");

        List<GPUInfo> gpuInfoList = getGpuInfos();
        String jsonString = JSON.toJSONString(gpuInfoList, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.UseSingleQuotes);
        model.addAttribute("cpulist",gpuInfoList);
        return "index";

    }



    @RequestMapping(value = "/EcharsShow")
    @ResponseBody
    public List<GPUInfo> findById(Model model) {
        List<GPUInfo> list = new ArrayList<GPUInfo>();
        list.add(new GPUInfo(12,"1023","12","32","12",31.2));
//        list.add(new GPUInfo("帽子",50));
//        list.add(new GPUInfo("鞋子",126));
//        list.add(new GPUInfo("毛衣",75));
//        list.add(new GPUInfo("羽绒服",201));
//        list.add(new GPUInfo("羊毛衫",172));
        System.err.println(list.toString());
        return list;
    }


    @GetMapping(value = "/Echars.do")
    public String echarts4(Model model){
        System.err.println("========开始");
        return "index";
    }




    public List<GPUInfo> getGpuInfos() throws IOException {
        String gpus = null;

        gpus = shellService.getGPU();
		//命令行调用后获取的信息
        /*String gpus = "Mon Jun  1 10:47:16 2020       \n" +
                "+-----------------------------------------------------------------------------+\n" +
                "| NVIDIA-SMI 418.87.01    Driver Version: 418.87.01    CUDA Version: 10.1     |\n" +
                "|-------------------------------+----------------------+----------------------+\n" +
                "| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |\n" +
                "| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |\n" +
                "|===============================+======================+======================|\n" +
                "|   0  TITAN V             Off  | 00000000:2D:00.0  On |                  N/A |\n" +
                "| 29%   43C    P8    27W / 250W |   1123MiB / 12035MiB |      0%      Default |\n" +
                "+-------------------------------+----------------------+----------------------+\n" +
                "|   1  GeForce RTX 208...  Off  | 00000000:99:00.0 Off |                  N/A |\n" +
                "|  0%   29C    P8    20W / 260W |     11MiB / 10989MiB |      0%      Default |\n" +
                "+-------------------------------+----------------------+----------------------+\n" +
                "                                                                               \n" +
                "+-----------------------------------------------------------------------------+\n" +
                "| Processes:                                                       GPU Memory |\n" +
                "|  GPU       PID   Type   Process name                             Usage      |\n" +
                "|=============================================================================|\n" +
                "|    0     16841      C   inference_worker                            1077MiB |\n" +
                "|    0     19996      G   /usr/lib/xorg/Xorg                            33MiB |\n" +
                "+-----------------------------------------------------------------------------+\n";*/
        //System.out.println("命令行获取的结果: " + gpus);
        //分割废物信息
        String[] split = gpus.split("\\|===============================\\+======================\\+======================\\|");
        String[] gpusInfo = split[1].split("                                                                               ");
        // 分割多个gpu
        String[] gpuInfo = gpusInfo[0].split("\\+-------------------------------\\+----------------------\\+----------------------\\+");
        //System.out.println("000000000000000000000000000000000");
        List<GPUInfo> gpuInfoList = new ArrayList<>();
        for (int i = 0; i < gpuInfo.length - 1; i++) {
            GPUInfo gpuInfo1 = new GPUInfo();
            String[] nameAndInfo = gpuInfo[i].split("\n");
            //只要第二块的数据
            /*0
             *TITAN
             *V
             *Off
             * */
            String[] split1 = nameAndInfo[1].split("\\|")[1] // 0  TITAN V             Off
                    .split("\\s+");//去空格

            gpuInfo1.setNumber(Integer.parseInt(split1[1]));
            StringBuffer name = new StringBuffer();
            for (int j = 0; j < split1.length - 1; j++) {
                if (j > 1 && j != split1.length) {
                    name.append(split1[j] + " ");
                }
            }
            gpuInfo1.setName(name.toString());

            String[] info = nameAndInfo[2].split("\\|")[2].split("\\s+");
            /* System.out.println("biubiu~~~biubiu~~~biubiu~~~biubiu~~~biubiu~~~biubiu~~~biubiu~~~biubiu~~~biubiu~~~biubiu~~~");*/
            gpuInfo1.setUsedMemory(info[1]);
            gpuInfo1.setTotalMemory(info[3]);
            int useable = Integer.parseInt(gpuInfo1.getTotalMemory().split("MiB")[0]) - Integer.parseInt(gpuInfo1.getUsedMemory().split("MiB")[0]);
            gpuInfo1.setUseableMemory(useable + "MiB");
            Double usageRate = Integer.parseInt(gpuInfo1.getUsedMemory().split("MiB")[0]) * 100.00 / Integer.parseInt(gpuInfo1.getTotalMemory().split("MiB")[0]);
            gpuInfo1.setUsageRate(usageRate);
            gpuInfoList.add(gpuInfo1);

        }
        return gpuInfoList;
    }
}


