package de.brokenpipe;

import java.io.File;
import java.io.FileNotFoundException;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public class Main {
	public static void main(final String[] args) throws FileNotFoundException {
		final var fis = new File("/home/stesie/workspace/cxc-ri/server/src/main/resources/processes-ri/offer/offer-1000.bpmn");

		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(fis);
		System.out.println("Hello world!");
	}
}