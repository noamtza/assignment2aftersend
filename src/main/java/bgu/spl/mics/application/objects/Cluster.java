package bgu.spl.mics.application.objects;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
//import javafx.util.Pair;
import jdk.internal.net.http.common.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**d
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {
	BlockingQueue<CPU> cpus;
	BlockingQueue<Pair<DataBatch, GPU>> processedDataQueue;
	BlockingQueue<Pair<DataBatch, GPU>> unprocessedDataQue;
	LinkedList<GPU> gpusALL = new LinkedList<>();
	LinkedList<CPU> cpusALL = new LinkedList<>();
	LinkedList<ConfrenceInformation> confrenceALL= new LinkedList<>();
	int avilableCpus;

	private ArrayList<String> trainedModels;
	private int totalDataBatches;
	private int cpuTimeUnitUsed;
	private int gpuTimeUnitUsed;
	//private static final Cluster instance = new Cluster();


	private static class ClusterInstance {
		private static final Cluster instance = new Cluster();
	}

	public Cluster() {
		//avilableCpus=cpus.size();
		cpus = new LinkedBlockingQueue<>();
		processedDataQueue = new LinkedBlockingQueue<Pair<DataBatch, GPU>>();
		unprocessedDataQue = new LinkedBlockingQueue<Pair<DataBatch, GPU>>();
		avilableCpus=0;

		trainedModels=new ArrayList<String>();
		totalDataBatches=0;
		cpuTimeUnitUsed=0;
		gpuTimeUnitUsed=0;

	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public void addGPUS (ArrayList<GPU> gpus){
		for(GPU gpu : gpus)
			gpusALL.add(gpu);

	}
	public void setAvilableCpus() {
		avilableCpus++;
	}
	public void addCPUS (BlockingQueue<CPU> cpus){
		for(CPU cpu : cpus)
			cpusALL.add(cpu);
	}
	public void addConference (ArrayList<ConfrenceInformation> confrences){
		for(ConfrenceInformation confrenceInformation : confrences)
			confrenceALL.add(confrenceInformation);
	}
	public static Cluster getInstance() {
		return ClusterInstance.instance;
	}


	public void addCpu(CPU c) {//the cpu is availible to get data;
		cpus.add(c);
	}

	public BlockingQueue<CPU> getCpus() {
		return cpus;
	}

	public void sendUnprocessedData(Pair<DataBatch, GPU> pair) {
		//CPU currCpu = null;
	//	try {
		if (pair != null) {
			//	unprocessedDataQue.add(pair);
			if (avilableCpus > 0) {
				CPU currCpu = cpus.poll();
				if (currCpu != null) {
					//cpus.add(currCpu);
					//if (unprocessedDataQue.size() > 0) {
					//	Pair<DataBatch, GPU> currPair = unprocessedDataQue.poll();
					if (avilableCpus > 0) {
						currCpu.getUnprossesedDataBatch(pair);
						avilableCpus--;
					}
				}
			}
		}
		//	}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

	}


//	public void addUnprocessedDataBatch(Pair<DataBatch, GPU> pair) {
//		if (pair != null){
//	//		System.out.println(pair.getValue().getModel());
//	//		System.out.println(unprocessedDataQue.size());
//			unprocessedDataQue.add(pair);
//		//	System.out.println(unprocessedDataQue.size());
//	}


	public void addprocessedDataBatch(Pair<DataBatch, GPU> pair) {
		if (pair != null) {
			processedDataQueue.add(pair);
			sendprocessedData();
		}
	}

	public void sendprocessedData() {
		if (processedDataQueue != null && processedDataQueue.size() > 0) {
			if (processedDataQueue.peek() != null) {
				GPU tosend = processedDataQueue.peek().getSecond();
				if (tosend != null) {
					tosend.getProcessedData(processedDataQueue.poll().getFirst());
				}
			}
		}
	}
	public void setCpus(BlockingQueue<CPU> listCpus){
		cpus=listCpus;
		avilableCpus=listCpus.size();
	}
	public BlockingQueue<CPU> getCPUS(){
		return cpus;
	}


	public int getTotalDataBatches(){
		return totalDataBatches;
	}
	public void addModel(Model model){
		trainedModels.add(model.getName());
	}

	public void setTotalDataBatches(){
		totalDataBatches++;
	}
	public void setCpuTimeUnitUsed(int time){
		cpuTimeUnitUsed+=time;
	}

	public void setGpuTimeUnitUsed(int time){
		gpuTimeUnitUsed+=time;
	}
	public int getCpuTimeUnitUsed(){
		return cpuTimeUnitUsed;
	}
	public int getGpuTimeUnitUsed(){
		return gpuTimeUnitUsed;
	}

}