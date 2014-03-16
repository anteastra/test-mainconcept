package com.mainconcept.cloud;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.mainconcept.cloud.loaders.MachinesLoader;
import com.mainconcept.cloud.loaders.MachinesLoaderFactory;
import com.mainconcept.cloud.model.MachineIdent;
import com.mainconcept.cloud.model.Message;
import com.mainconcept.cloud.model.Message.MessageType;
import com.mainconcept.cloud.model.TCPMachineIdent;

public class CloudKiller {

	private static final String MACHINES_KEY = "-fmachines";

	public static void main(String ... args) {
		
		System.out.println();
		System.out.println();
		System.out.println("---------Shutdown cloud---------");
		
		if ((args.length < 2) || (!args[0].toLowerCase().equals(MACHINES_KEY))) {
			throw new IllegalArgumentException(MACHINES_KEY + " file expected");
		}
		
		MachinesLoader machinesLoader = MachinesLoaderFactory.getMachinesLoader(args[1]);
		if (machinesLoader.getMachineIdentList() == null) {
			System.out.println("error in machine loader");
			System.exit(0);
		}
		
		if (machinesLoader.getMachineIdentList().size() == 0) {
			System.out.println("there is no machine start");
			System.exit(0);
		}		
		
		for (MachineIdent mi: machinesLoader.getMachineIdentList()) {
			downMachine(mi);
		}
	}
	
	public static void downMachine(MachineIdent mi) {
		try {
			@SuppressWarnings("resource")
			Socket s = new Socket("localhost", ((TCPMachineIdent)mi).getPort());
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
			os.writeObject(new Message(MessageType.REQUEST_PERFORM_SHUTDOWN, null, null));
			
			ObjectInputStream is = new ObjectInputStream(s.getInputStream());
			Message msg = (Message) is.readObject();
			
			System.out.println(msg.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
