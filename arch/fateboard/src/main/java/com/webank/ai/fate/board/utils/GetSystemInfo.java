package com.webank.ai.fate.board.utils;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

public class GetSystemInfo {


   static  Logger logger  = LoggerFactory.getLogger(GetSystemInfo.class);

    public static String getLocalIp() {

        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();

                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {

                        if (!ip.getHostAddress().equals("127.0.0.1")
                                && !ip.getHostAddress().equals("0.0.0.0")) {
                             return  ip.getHostAddress();
                        }

                    }
                }
            }
        } catch (Throwable e) {

            logger.error("get local ip error",e);
        }
        return null;


    }



    /** 
     * 获取操作系统名称 
     * @return 
     */  
    public static String getOsName(){  
        // 操作系统  
        String osName = System.getProperty("os.name");  
        return osName;  
    }  
  
    /** 
     * 获取系统cpu负载 
     * @return 
     */  
    public static double getSystemCpuLoad(){  
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory  
                .getOperatingSystemMXBean();  
        double SystemCpuLoad = osmxb.getSystemCpuLoad();  
        return SystemCpuLoad;  
    }  
  
    /** 
     * 获取jvm线程负载 
     * @return 
     */  
    public static double getProcessCpuLoad(){  
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory  
                .getOperatingSystemMXBean();  
        double ProcessCpuLoad = osmxb.getProcessCpuLoad();  
        return ProcessCpuLoad;  
    }  
  
    /** 
     * 获取总的物理内存 
     * @return 
     */  
    public static long getTotalMemorySize(){  
        int kb = 1024;  
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory  
                .getOperatingSystemMXBean();  
        // 总的物理内存  
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;  
        return totalMemorySize;  
    }  
  
    /** 
     * 获取剩余的物理内存 
     * @return 
     */  
    public static long getFreePhysicalMemorySize(){  
        int kb = 1024;  
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory  
                .getOperatingSystemMXBean();  
        // 剩余的物理内存  
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;  
        return freePhysicalMemorySize;  
    }  
  
    /** 
     * 获取已使用的物理内存 
     * @return 
     */  
    public static long getUsedMemory(){  
        int kb = 1024;  
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory  
                .getOperatingSystemMXBean();  
        //已使用的物理内存  
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;  
        return usedMemory;  
    }


    public  static   void   test(){


        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        memoryPoolMXBeans.forEach((pool) -> {
            System.out.println(pool.getName());
            System.out.println(pool.getUsage());
        });


    }


    public  static  void  main(String[]  args){

       System.err.println( getUsedMemory());

       System.err.println(getFreePhysicalMemorySize());

       System.err.println(getProcessCpuLoad());

       System.err.println(getSystemCpuLoad());

       System.err.println(getLocalIp());
       test();

    }
}  