package se.sundsvall.citizenchanges.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class BatchContext {

	private int firstErrand;

	private int numberOfErrands;

	private boolean sendMessages;

	private List<String> oepErrandIds;

	private String sms;

	private String email;

}
