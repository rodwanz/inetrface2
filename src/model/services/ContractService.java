package model.services;

import java.util.Calendar;
import java.util.Date;

import model.entities.Contract;
import model.entities.Installment;

public class ContractService {
	
	private OnlinePaymentService onlinePaymentService;
		
	public ContractService(OnlinePaymentService onlinePaymentService) {
		this.onlinePaymentService = onlinePaymentService;
	}

	public void processContract(Contract contract, int months) {
		double basicQuota = contract.getTotalValue() / months;// basicQuota = 200
		for (int i = 1; i <= months; i++) {
			double updateQuota = basicQuota + onlinePaymentService.interest(basicQuota, i);// updateQuota = 202
			double fullQuota = basicQuota + onlinePaymentService.paymentFee(updateQuota);// fullQuota = 206.04
			Date dueDate = addMonths(contract.getDate(), i);
			contract.getInstallment().add(new Installment(dueDate, fullQuota));
		}
	}
	
	private Date addMonths(Date date, int N) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, N);
		return cal.getTime();
	}
}
