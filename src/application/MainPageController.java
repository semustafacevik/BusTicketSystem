package application;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainPageController {
	SQLQueries SQL = new SQLQueries();
	Sefer sefer = new Sefer();

	ObservableList<Bus> BusList = FXCollections.observableArrayList();
	ObservableList<Bus> BusList_JM = FXCollections.observableArrayList();
	ObservableList<Rota> JourneyList = FXCollections.observableArrayList();

	ObservableList<String> Journey_FromList = FXCollections.observableArrayList();
	ObservableList<String> Journey_ToList = FXCollections.observableArrayList();

	Bus bus;
	Rota rota = new Rota();
	JourneyBus jb;
	JourneyBus journeyBus = new JourneyBus();

	ArrayList<Button> Buttons = new ArrayList<Button>();

	@FXML
	private AnchorPane paneTravelerInfo;	
	@FXML
	private ScrollPane SeatScrollPane;
	@FXML
	private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,
	btn15,btn16,btn17,btn18,btn19,btn20,btn21,btn22,btn23,btn24,btn25,btn26,btn27,btn28,btn29,
	btn30,btn31,btn32,btn33,btn34,btn35,btn36,btn37,btn38,btnAddJourney,btnDeleteBus,btnBusList;
	@FXML
	private DatePicker dateJourneyDate,dateJourneyDate_JM,dateJourneyDate_JMU;
	@FXML
	private CheckBox chkMale,chkFemale;
	@FXML
	private TextField txtTCKN,txtName,txtLastname,txtPlate,txtModel,txtTCKN_Ticket,txtJourneyTime,
	txtJourneyPrice,txtJourneyTime_JMU,txtJourneyPrice_JMU;
	@FXML
	private Label lblTicketName,lblTicketFr_To,lblTicketTime,lblTicketDate,lblTicketPrice,lblBus_JMU,
	lblDate_JMU,lblTime_JMU,lblPrice_JMU;
	@FXML
	private ListView<String> lstJourney;
	@FXML
	private ComboBox<Sefer> cmbSearch;
	@FXML
	private ComboBox<Bus> cmbBus,cmbBus_JM;
	@FXML
	private ComboBox<String> cmbFrom,cmbTo,cmbFrom_JM,cmbTo_JM;
	@FXML
	private ComboBox<Rota> cmbJourney_JMU;
	@FXML
	private ComboBox<Bus> cmbBus_JMU;


	////// METHODS

	/// RESERVATÝON PAGE

	@FXML
	void Reservation(Event event) throws SQLException {
		EkraniSifirla();
	}

	@FXML
	void Journey_FromSelect(ActionEvent event) throws SQLException {
		SeatScrollPane.setVisible(false);
		paneTravelerInfo.setVisible(false);
		cmbTo.getItems().clear();
		rota.baslangic = cmbFrom.getValue();
		ResultSet resultSet = SQL.GetTo_Journey(rota.baslangic);
		while(resultSet.next()) {
			Journey_ToList.add(resultSet.getString("bitis"));
		}
		cmbTo.setItems(Journey_ToList);
	}

	@FXML
	void Journey_ToSelect(ActionEvent event) {
		SeatScrollPane.setVisible(false);
		paneTravelerInfo.setVisible(false);
		rota.bitis = cmbTo.getValue();		
	}

	@FXML
	void Journey_DateSelect(ActionEvent event) {
		SeatScrollPane.setVisible(false);
		paneTravelerInfo.setVisible(false);
		rota.tarih = dateJourneyDate.getValue();
	}

	@FXML
	void Journey_Search(ActionEvent event) throws SQLException
	{  	SeatScrollPane.setVisible(false);
		paneTravelerInfo.setVisible(false);
		ResultSet resultSet = SQL.Select(rota.baslangic, rota.bitis, rota.tarih.toString());
		ObservableList<Sefer> Seferler = FXCollections.observableArrayList();
		while(resultSet.next()) 
		{
			sefer.seferID =  resultSet.getString("seferID");  		
			sefer.otobusMarka = resultSet.getString("otobusMarka");
			sefer.kalkisSaati = resultSet.getString("kalkisSaati");
			sefer.seferUcreti = resultSet.getString("seferUcreti");
			sefer.rotaID = resultSet.getString("rotaID");

			Seferler.addAll(sefer);
		}
		cmbSearch.setItems(Seferler);
		cmbSearch.setConverter(new StringConverter<Sefer>() {
			@Override
			public Sefer fromString(String string) {
				return cmbSearch.getItems().stream().filter(ap -> 
				ap.otobusMarka.equals(string)).findFirst().orElse(null);
			}
			@Override
			public String toString(Sefer seferObj) {
				return seferObj.otobusMarka + "  " + seferObj.kalkisSaati + "  " + seferObj.seferUcreti;
			}
		});

	}
	String seferID;
	@FXML
	void Journey_BusSelected(ActionEvent event) throws SQLException {
		seferID = cmbSearch.getValue().seferID; 
		SeatScrollPane.setVisible(true);		
		ButonlariListeyeEkle();

		ResultSet resultset = SQL.TicketControlForSeat(seferID);
		while(resultset.next())
		{   
			for(Button btn : Buttons) {
				if(btn==null)
					break;
				if(btn.getText().equals(resultset.getString("koltukNumarasi")))
				{
					if(resultset.getString("cinsiyet").equals("0")) {
						btn.setStyle("-fx-background-color: purple;");
						btn.setDisable(true);
					}
					else{
						btn.setStyle("-fx-background-color: darkblue");
						btn.setDisable(true);
					}


				}
			}
		}
	}

	Button SecilenKoltuk;
	@FXML
	void SeatClick(ActionEvent event) throws SQLException
	{
		SeatScrollPane.setVisible(false);
		paneTravelerInfo.setVisible(true);

		SecilenKoltuk = (Button) event.getSource();		
	}

	@FXML
	void BuyTicketClick(ActionEvent event) throws SQLException, IOException {
		String sex;
		if(chkFemale.isSelected()) {
			sex="False";
		}
		else {
			sex="True";
		}	
		SQL.InsertTraveler(txtName.getText(), txtLastname.getText(),sex,txtTCKN.getText(),SecilenKoltuk.getText());
		ResultSet resultset_Yolcu = SQL.SelectTraveler(txtTCKN.getText());

		String yolcuId = "";
		while(resultset_Yolcu.next()){
			yolcuId=resultset_Yolcu.getString("yolcuID");
		}
		if(yolcuId!="") {
			SQL.InsertTicket(yolcuId,seferID);
			ResultSet resultSet_Bilet = SQL.GetTicket(seferID);
			while(resultSet_Bilet.next()) {
				String sex_Str;
				if(chkMale.isSelected()) {
					sex_Str = "Erkek";
					SecilenKoltuk.setStyle("-fx-background-color: darkblue;");
				}
				else {
					sex_Str = "Kadýn";
					SecilenKoltuk.setStyle("-fx-background-color: purple;");
					}
				FXMLLoader Loader = new FXMLLoader();
				Loader.setLocation(getClass().getResource("Ticket.fxml"));
				try {
					Loader.load();
				}
				catch(Exception e) {
					e.getMessage();
				}
				TicketController tickCont= Loader.getController();

				Parent parent = Loader.getRoot();
				Stage stage= new Stage();
				stage.setTitle("TICKET");
				stage.setScene(new Scene(parent));
				tickCont.SetText(txtTCKN.getText(),txtName.getText() +" "+txtLastname.getText(), sex_Str,
						resultSet_Bilet.getString("Baslangic"), resultSet_Bilet.getString("Bitis"), 
						resultSet_Bilet.getString("kalkisSaati"), resultSet_Bilet.getString("kalkisTarihi"),
						resultSet_Bilet.getString("seferUcreti"), SecilenKoltuk.getText());
				stage.showAndWait();
			}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setContentText("Bought Ticket");
			alert.showAndWait();
		}
		SecilenKoltuk.setDisable(true);
		SeatScrollPane.setVisible(true);
		paneTravelerInfo.setVisible(false);
		txtTCKN.clear();
		txtName.clear();
		txtLastname.clear();

		EkraniSifirla();
	}

	@FXML
	void CancelTicketClick(ActionEvent event) {
		SeatScrollPane.setVisible(true);
		paneTravelerInfo.setVisible(false);
		txtTCKN.clear();
		txtName.clear();
		txtLastname.clear();
	}

	@FXML
	void Seat_MEntered(Event event) {
		Button btn = (Button) event.getSource();
		btn.setStyle("-fx-background-color: yellow;");
	}

	@FXML
	void Seat_MExited(Event event) {
		Button btn = (Button) event.getSource();
		btn.setStyle("-fx-background-color: lightgray;");
	}


	/// ---------------------------


	/// BUS MANAGEMENT

	@FXML	
	void BusManagement(Event event) throws SQLException {
		txtPlate.clear();
		txtModel.clear();
		cmbBus.getItems().clear();
		lstJourney.getItems().clear();
		OtobusleriGetir();
	}

	@FXML
	void AddBusClick(ActionEvent event) throws SQLException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText("Added Bus");
		alert.showAndWait();
		
		SQL.InsertBus(txtPlate.getText(), txtModel.getText());	
		txtPlate.clear();
		txtModel.clear();
		OtobusleriGetir();
		
		
	}

	@FXML
	void BusSelect(ActionEvent event) throws SQLException {
		lstJourney.getItems().clear();
		String busID = cmbBus.getValue().OtobusID;
		btnDeleteBus.setDisable(false);
		ResultSet resultSet = SQL.GetAllJourneyByID(busID);
		while(resultSet.next()) {
			lstJourney.getItems().add("Journey: " + resultSet.getString("baslangic") + " - " + resultSet.getString("bitis")
			+ "  //  Date: " + resultSet.getString("kalkisTarihi") 
			+ "  //  Time: " + resultSet.getString("kalkisSaati"));
		}
	}

	@FXML
	void DeleteBus(ActionEvent event) throws SQLException {
		String busId=cmbBus.getValue().OtobusID;
		SQL.DeleteBusandOthers(busId);
		lstJourney.getItems().clear();
		OtobusleriGetir();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText("Deleted Bus");
		alert.showAndWait();
	}


	/// ---------------------------



	/// JOURNEY MANAGEMENT

	@FXML
	void JourneyManagement(Event event) throws SQLException {
		cmbTo_JM.getItems().clear();
		cmbBus_JM.getItems().clear();
		cmbJourney_JMU.getItems().clear();
		dateJourneyDate_JM.getEditor().clear();
		txtJourneyTime.clear();
		txtJourneyPrice.clear();

		lblDate_JMU.setText("-");
		lblTime_JMU.setText("-");
		lblPrice_JMU.setText("-");
		dateJourneyDate_JMU.getEditor().clear();
		txtJourneyTime_JMU.clear();
		txtJourneyPrice_JMU.clear();
		
		
		cmbFrom_JM.setItems(Journey_FromList);

		ResultSet resultSet = SQL.GetAllJourney();
		while(resultSet.next()) {
			Rota rota =new Rota();
			rota.baslangic=resultSet.getString("baslangic");
			rota.bitis=resultSet.getString("bitis");
			rota.rotaID=resultSet.getString("rotaID");
			JourneyList.add(rota);
		}
		cmbJourney_JMU.setItems(JourneyList);
		cmbJourney_JMU.setConverter(new StringConverter<Rota>() {
			@Override
			public Rota fromString(String string) {
				return cmbJourney_JMU.getItems().stream().filter(ap -> 
				ap.rotaID.equals(string)).findFirst().orElse(null);
			}
			@Override
			public String toString(Rota rota_Obj) {
				return rota_Obj.baslangic + " - " + rota_Obj.bitis;
			}
		});


	}

	@FXML
	void JM_FromSelect(ActionEvent event) throws SQLException {
		cmbTo_JM.getItems().clear();
		journeyBus.baslangic = cmbFrom_JM.getValue();
		ResultSet resultSet = SQL.GetTo_Journey(journeyBus.baslangic);
		while(resultSet.next()) {
			Journey_ToList.add(resultSet.getString("bitis"));
		}
		cmbTo_JM.setItems(Journey_ToList);
	}

	@FXML
	void JM_ToSelect(ActionEvent event) throws SQLException {
		journeyBus.bitis = cmbTo_JM.getValue();
	}

	@FXML
	void JM_DateSelect(ActionEvent event) {
		journeyBus.kalkisTarihi = dateJourneyDate_JM.getValue().toString();
	}

	@FXML
	void JM_ListOfBuses(ActionEvent event) throws SQLException {
		cmbBus_JM.getItems().clear();
		ResultSet resultset=SQL.GetAllActiveBus();
		while(resultset.next()) {
			bus = new Bus();
			bus.OtobusID = resultset.getString("otobusID");
			bus.Marka=resultset.getString("otobusMarka");
			bus.Plaka=resultset.getString("otobusPlaka");

			BusList_JM.add(bus);
		}

		ResultSet resultSet_JM = SQL.BusControlForJM(journeyBus.kalkisTarihi.toString());


		while(resultSet_JM.next()){			
			for(Iterator<Bus> itr = BusList_JM.iterator(); itr.hasNext();) {
				Bus dlt_b = itr.next();
				if(dlt_b.OtobusID.equals(resultSet_JM.getString("otobusID"))) {
					itr.remove();
				}
			}
		}

		cmbBus_JM.setItems(BusList_JM);
		cmbBus_JM.setConverter(new StringConverter<Bus>() {
			@Override
			public Bus fromString(String string) {
				return cmbBus_JM.getItems().stream().filter(ap -> 
				ap.OtobusID.equals(string)).findFirst().orElse(null);
			}
			@Override
			public String toString(Bus busObj) {
				return busObj.Plaka + "  " + busObj.Marka;
			}
		});
	}

	@FXML
	void JM_BusSelect(ActionEvent event) throws SQLException {
		journeyBus.otobusID = cmbBus_JM.getValue().OtobusID; 
		journeyBus.kalkisSaati = txtJourneyTime.getText();
		journeyBus.seferUcreti = txtJourneyPrice.getText();

		ResultSet resultSet = SQL.JourneyControlByFr_To(journeyBus.baslangic, journeyBus.bitis);
		while(resultSet.next()) {
			journeyBus.rotaID = resultSet.getString("rotaID");
		}
	}

	@FXML
	void JM_AddJourney(ActionEvent event) throws SQLException {
		SQL.InsertJourney(journeyBus.rotaID, journeyBus.kalkisTarihi.toString(), journeyBus.kalkisSaati, journeyBus.seferUcreti, journeyBus.otobusID);
	}



	ObservableList<Bus> BusListOfJourney = FXCollections.observableArrayList();

	@FXML
	void JMU_JourneySelect(ActionEvent event) throws SQLException {
		cmbBus_JMU.getItems().clear();
		ResultSet result = SQL.GetBusforRotaId(cmbJourney_JMU.getValue().rotaID);

		while(result.next()) {
			Bus bus= new Bus();	
			bus.Marka=result.getString("otobusMarka");
			bus.Plaka=result.getString("otobusPlaka");
			bus.OtobusID=result.getString("otobusID");

			BusListOfJourney.add(bus);
		}

		cmbBus_JMU.setItems(BusListOfJourney);
		cmbBus_JMU.setConverter(new StringConverter<Bus>() {
			@Override
			public Bus fromString(String string) {
				return cmbBus_JMU.getItems().stream().filter(ap -> 
				ap.OtobusID.equals(string)).findFirst().orElse(null);

			}
			@Override
			public String toString(Bus busObj) {
				return  busObj.Marka+"  "+busObj.Plaka;
			}
		});


	}

	String updateSeferID="";
	@FXML
	void JMU_BusSelect(ActionEvent event) throws SQLException {
		String BusID= cmbBus_JMU.getValue().OtobusID;
		ResultSet resultset=SQL.GetAllJourneyByID(BusID);

		while(resultset.next()) {

			updateSeferID = resultset.getString("seferID");
			lblDate_JMU.setText(resultset.getString("kalkisTarihi"));
			lblTime_JMU.setText(resultset.getString("kalkisSaati"));
			lblPrice_JMU.setText(resultset.getString("seferUcreti"));

		}

	}

	@FXML
	void JMU_UpdateJourney(ActionEvent event) throws SQLException {
		SQL.UpdateJourney(updateSeferID,dateJourneyDate_JMU.getValue().toString(),txtJourneyTime_JMU.getText(),txtJourneyPrice_JMU.getText());

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText("Updated Journey");
		alert.showAndWait();
		
		cmbJourney_JMU.getItems().clear();
		dateJourneyDate_JM.getEditor().clear();
		txtJourneyTime.clear();
		txtJourneyPrice.clear();
		lblDate_JMU.setText("-");
		lblTime_JMU.setText("-");
		lblPrice_JMU.setText("-");
		dateJourneyDate_JMU.getEditor().clear();
		txtJourneyTime_JMU.clear();
		txtJourneyPrice_JMU.clear();
	}

	/// ---------------------------


	/// TICKET

	@FXML
	void Ticket(Event event) {
		txtTCKN_Ticket.clear();
		lblTicketDate.setText("-");
		lblTicketFr_To.setText("-");
		lblTicketName.setText("-");
		lblTicketPrice.setText("-");
		lblTicketTime.setText("-");
	}

	String SilinecekBiletId="";
	@FXML
	void TicketFind(ActionEvent event) throws SQLException {
		String ticketTCKN = txtTCKN_Ticket.getText();

		ResultSet resultSet = SQL.GetBiletByTCKN(ticketTCKN);

		while(resultSet.next()) {
			SilinecekBiletId=resultSet.getString("biletID");
			lblTicketPrice.setText(resultSet.getString("seferUcreti"));
			lblTicketName.setText(resultSet.getString("adSoyad"));
			lblTicketFr_To.setText(resultSet.getString("Baslangic")+"-"+resultSet.getString("Bitis"));
			lblTicketDate.setText(resultSet.getString("kalkisTarihi"));
			lblTicketTime.setText(resultSet.getString("kalkisSaati"));
		}

	}

	@FXML
	void DeleteTicket (ActionEvent event) throws SQLException {
		SQL.DeleteBilet(SilinecekBiletId);
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText("Deleted Ticket");
		alert.showAndWait();
		
		txtTCKN_Ticket.clear();
		lblTicketDate.setText("-");
		lblTicketFr_To.setText("-");
		lblTicketName.setText("-");
		lblTicketPrice.setText("-");
		lblTicketTime.setText("-");
	}


	///-------------------------



	/// Ready Methods

	void OtobusleriGetir() throws SQLException {
		cmbBus.getItems().clear();
		ResultSet resultset=SQL.GetAllBus();
		while(resultset.next()) {
			bus= new Bus();
			bus.OtobusID = resultset.getString("otobusID");
			bus.Marka=resultset.getString("otobusMarka");
			bus.Plaka=resultset.getString("otobusPlaka");
			String Aktif=resultset.getString("isActive");
			if(Aktif.equals("1"))
				bus.isActive="Active";
			else
				bus.isActive="Inactive";

			BusList.add(bus);
		}
		cmbBus.setItems(BusList);
		cmbBus.setConverter(new StringConverter<Bus>() {
			@Override
			public Bus fromString(String string) {
				return cmbBus.getItems().stream().filter(ap -> 
				ap.OtobusID.equals(string)).findFirst().orElse(null);
			}
			@Override
			public String toString(Bus busObj) {
				return busObj.Marka + "  " + busObj.Plaka + "  (" + busObj.isActive + ")";
			}
		});
	}
	void EkraniSifirla() throws SQLException {
		cmbFrom.getItems().clear();
		cmbTo.getItems().clear();
		dateJourneyDate.getEditor().clear();
		cmbSearch.getItems().clear();
		SeatScrollPane.setVisible(false);
		paneTravelerInfo.setVisible(false);

		ResultSet resultSet = SQL.GetFrom_Journey();
		while(resultSet.next()) {		
			Journey_FromList.add(resultSet.getString("baslangic"));
		}
		cmbFrom.setItems(Journey_FromList);
	}
	void ButonlariListeyeEkle() {
		Buttons.add(btn1); 
		Buttons.add(btn2);
		Buttons.add(btn3);
		Buttons.add(btn4);
		Buttons.add(btn5);
		Buttons.add(btn6);
		Buttons.add(btn7);
		Buttons.add(btn8);
		Buttons.add(btn9);
		Buttons.add(btn10);
		Buttons.add(btn11);
		Buttons.add(btn12);
		Buttons.add(btn13);
		Buttons.add(btn14);
		Buttons.add(btn15);
		Buttons.add(btn16);
		Buttons.add(btn17);
		Buttons.add(btn18);
		Buttons.add(btn19);
		Buttons.add(btn20);
		Buttons.add(btn21);
		Buttons.add(btn22);
		Buttons.add(btn23);
		Buttons.add(btn24);
		Buttons.add(btn25);
		Buttons.add(btn26);
		Buttons.add(btn27);
		Buttons.add(btn28);
		Buttons.add(btn29);
		Buttons.add(btn30);
		Buttons.add(btn31);
		Buttons.add(btn32);
		Buttons.add(btn33);
		Buttons.add(btn34);
		Buttons.add(btn35);
		Buttons.add(btn36);
		Buttons.add(btn37);
		Buttons.add(btn38);
	}
}
