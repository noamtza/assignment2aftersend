package bgu.spl.mics.application;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;

import java.io.*;

import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
//import org.json.JSONArray;
//import org.json.JSONObject;

import java.io.FileReader;
import java.nio.file.FileSystemNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static Cluster cluster = Cluster.getInstance();

    public static CountDownLatch countInit;

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File input = new File(args[0]);
        try {
            ArrayList<Student> Students = new ArrayList<>();
            ArrayList<Model> models = new ArrayList<>();
            ArrayList<GPU> GPUS = new ArrayList<>();
            BlockingQueue<CPU> CPUS = new LinkedBlockingQueue<>();
            ArrayList<ConfrenceInformation> Conferences = new ArrayList<>();
            int Duration = 0;
            int TickTime = 0;
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();
            Duration = fileObject.get("Duration").getAsInt();
            TickTime = fileObject.get("TickTime").getAsInt();
            JsonArray gpus = fileObject.get("GPUS").getAsJsonArray();
            for (JsonElement GPUElement : gpus) {
                String typeOfGpu = GPUElement.getAsString();
                GPU.Type gpuType = GPU.Type.valueOf(typeOfGpu);
                GPU curGpu = new GPU(gpuType);
                GPUS.add(curGpu);
            }
            JsonArray cpus = fileObject.get("CPUS").getAsJsonArray();
            for (JsonElement CPUElement : cpus) {
                int coreOfcpu = CPUElement.getAsInt();
                CPU curCpu = new CPU(coreOfcpu);
                CPUS.add(curCpu);
            }
            Cluster c = Cluster.getInstance();
            System.out.println(CPUS.size());
            c.setCpus(CPUS);
            JsonArray jsonarrayOfStud = fileObject.get("Students").getAsJsonArray();
            for (JsonElement StudentElement : jsonarrayOfStud) {
                JsonObject studentOb = StudentElement.getAsJsonObject();
                String studName = studentOb.get("name").getAsString();
                String department = studentOb.get("department").getAsString();
                String studenStatus = studentOb.get("status").getAsString();
                Student s;
                if (studenStatus.charAt(0) == 'M') {
                    s = new Student(studName, department, Student.Degree.MSc);
                } else
                    s = new Student(studName, department, Student.Degree.PhD);
                ArrayList<Model> studModels = new ArrayList<>();
                JsonArray jsonarrayOfModel = studentOb.get("models").getAsJsonArray();
                for (JsonElement ModelElement : jsonarrayOfModel) {
                    JsonObject modelOb = ModelElement.getAsJsonObject();
                    String modelName = modelOb.get("name").getAsString();
                    String reType = modelOb.get("type").getAsString();
                    int size = modelOb.get("size").getAsInt();
                    Data ModelData;
                    if (reType.charAt(0) == 'I') {
                        ModelData = new Data(size, Data.Type.Images);
                    } else if (reType.charAt(0) == 'T' && reType.charAt(1) == 'e') {
                        ModelData = new Data(size, Data.Type.Text);
                    } else {
                        ModelData = new Data(size, Data.Type.Tabular);
                    }
                    Model currModel = new Model(modelName, ModelData, s);
                    models.add(currModel);//addStudent
                    studModels.add(currModel);
                }
                s.setModels(studModels);
                Students.add(s);
            }
            JsonArray jsonArrayConferences = fileObject.get("Conferences").getAsJsonArray();
            for (JsonElement ConferenceElement : jsonArrayConferences) {
                JsonObject conferenceOb = ConferenceElement.getAsJsonObject();
                String name = conferenceOb.get("name").getAsString();
                int date = conferenceOb.get("date").getAsInt();
                ConfrenceInformation confInfo = new ConfrenceInformation(name, date);
                Conferences.add(confInfo);
            }
            c.setCpus(CPUS);
            List<Thread> StudentServiceThreads = new ArrayList<>();
            List<Thread> GpuServiceThreads = new ArrayList<>();
            List<Thread> CpuServiceThreads = new ArrayList<>();
            List<StudentService> studentServices = new ArrayList<>();
            List<GPUService> gpuServices = new ArrayList<>();
            List<CPUService> cpuServices = new ArrayList<>();
            List<ConferenceService> conferenceServices = new ArrayList<>();
            List<Thread> ConferenceServiceThreads = new ArrayList<>();
            for (int i = 0; i < Students.size(); i++) {
                studentServices.add(new StudentService("studentService" + i, Students.get(i),Students));
            }
            for (int i = 0; i < Students.size(); i++) {
                StudentServiceThreads.add(new Thread(studentServices.get(i)));
            }
            for (int i = 0; i < GPUS.size(); i++) {
                gpuServices.add(new GPUService("gpuService" + i, GPUS.get(i)));
            }
            for (int i = 0; i < GPUS.size(); i++) {
                GpuServiceThreads.add(new Thread(gpuServices.get(i)));
            }
            for (int i = 0; i < CPUS.size(); i++) {
                cpuServices.add(new CPUService("cpuService" + i, CPUS.poll()));
            }
            for (int i = 0; i < CPUS.size(); i++) {
                CpuServiceThreads.add(new Thread(cpuServices.get(i)));
            }
            for (int i = 0; i < Conferences.size(); i++) {
                conferenceServices.add(new ConferenceService("conferenceService" + i, Conferences.get(i)));
            }
            for (int i = 0; i < Conferences.size(); i++) {
                ConferenceServiceThreads.add(new Thread(conferenceServices.get(i)));
            }
            cluster.addCPUS(CPUS);
            cluster.addGPUS(GPUS);
            cluster.addConference(Conferences);

            // countInit=new CountDownLatch(GpuServiceThreads.size()+Conferences.size());
            countInit = new CountDownLatch(2);
            TimeService timeService = new TimeService();
            timeService.setDuration(Duration);
            timeService.setTickTime(TickTime);
            Thread time = new Thread(timeService);
            time.start();
//            GpuServiceThreads.get(0).start();
//            ConferenceServiceThreads.get(0).start();
            for(int i=0;i<GpuServiceThreads.size();i++){
                GpuServiceThreads.get(i).start();
            }
            for(int i=0;i<CpuServiceThreads.size();i++){
                CpuServiceThreads.get(i).start();
            }
            for(int i=0;i<ConferenceServiceThreads.size();i++){
                ConferenceServiceThreads.get(i).start();
            }

            for(int i=0;i<StudentServiceThreads.size();i++){
//            for (int i = 0; i < 1; i++) {
                try {
                    countInit.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StudentServiceThreads.get(i).start();
            }

//            CpuServiceThreads.get(0).start();
//            time.start();
//            StudentServiceThreads.get(0).start();
            try {
                time.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Gson gsonoOutput = new GsonBuilder().setPrettyPrinting().create();
                FileWriter writer = new FileWriter("output.json");
                String studentString = "Students: ";
                gsonoOutput.toJson(studentString, writer);
                for (int i = 0; i < Students.size(); i++)
                    gsonoOutput.toJson(Students.get(i), writer);
                String conferenceString = "Conferences: ";
                gsonoOutput.toJson(conferenceString, writer);
                for (int i = 0; i < Conferences.size(); i++)
                    gsonoOutput.toJson(Conferences.get(i), writer);


                String batches = "BatchesProcessed: ";
                gsonoOutput.toJson(batches, writer);
                gsonoOutput.toJson(cluster.getTotalDataBatches(), writer);

                String cpuTime = "CPU_TimeUsed: ";
                gsonoOutput.toJson(cpuTime, writer);
                gsonoOutput.toJson(cluster.getCpuTimeUnitUsed(), writer);

                String gpuTime = "GPU_TimeUsed :";
                gsonoOutput.toJson(gpuTime, writer);
                gsonoOutput.toJson(cluster.getGpuTimeUnitUsed(), writer);

                writer.flush();
                writer.close();
            } catch (Exception e) {
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        System.exit(0);

    }
}