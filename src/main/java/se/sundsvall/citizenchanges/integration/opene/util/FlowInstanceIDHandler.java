package se.sundsvall.citizenchanges.integration.opene.util;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

@Getter
public class FlowInstanceIDHandler extends DefaultHandler {

	private static final String FLOW_INSTANCE_ID = "flowInstanceID";

	private List<String> flowInstanceIDs;
	private boolean isFlowInstanceID;

	@Override
	public void startDocument() {
		flowInstanceIDs = new ArrayList<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (qName.equalsIgnoreCase(FLOW_INSTANCE_ID)) {
			isFlowInstanceID = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		if (isFlowInstanceID) {
			flowInstanceIDs.add(new String(ch, start, length));
			isFlowInstanceID = false;
		}
	}

}
