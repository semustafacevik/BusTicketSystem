package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicketController {
	@FXML
	private Label lblTCKN, lblName, lblSex, lblFrom, lblTo, lblTime, lblDate, lblPrice,lblSeat;

	public void SetText(String TCKN, String Name, String Sex, String From, String To, String Time, String Date, String Price, String Seat) {
		lblTCKN.setText(TCKN);
		lblName.setText(Name);
		lblSex.setText(Sex);
		lblFrom.setText(From);
		lblTo.setText(To);
		lblTime.setText(Time);
		lblDate.setText(Date);
		lblPrice.setText(Price);
		lblSeat.setText(Seat);
	}
}
