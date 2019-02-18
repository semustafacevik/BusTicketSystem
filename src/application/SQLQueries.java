package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLQueries {

	public ResultSet Select(String baslangic, String bitis, String tarih) throws SQLException {
		String query = "SELECT S.seferID,O.otobusMarka, S.kalkisSaati, S.seferUcreti,S.rotaID "
				+ "FROM tblSefer S INNER JOIN tblRota R ON S.rotaID = R.rotaID "
				+ "INNER JOIN tblOtobus O ON S.otobusID = O.otobusID "
				+ "WHERE R.baslangic = '" + baslangic + "' AND R.bitis = '" + bitis + "' AND S.kalkisTarihi = '" + tarih + "'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}


	public ResultSet BusControlForJM(String tarih) throws SQLException{
		String query = "SELECT O.otobusID "
				+ "FROM tblSefer S INNER JOIN tblRota R ON S.rotaID = R.rotaID "
				+ "INNER JOIN tblOtobus O ON S.otobusID = O.otobusID "
				+ "WHERE S.kalkisTarihi = '" + tarih + "'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}

	public ResultSet JourneyControlByFr_To(String baslangic, String bitis) throws SQLException {
		String query = "SELECT R.rotaID "
				+ "FROM tblRota R WHERE R.baslangic = '" + baslangic + "' AND R.bitis = '" + bitis + "'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}

	public void InsertJourney(String rotaID, String kalkisTarihi, String kalkisSaati, String seferUCreti, String otobusID) throws SQLException {

		String query = "INSERT INTO tblSefer (rotaID,kalkisTarihi,kalkisSaati,seferUcreti,otobusID)"
				+ " values('"+rotaID+"','"+kalkisTarihi+"','"+kalkisSaati+"','"+seferUCreti+"','"+otobusID+"')";
		Statement statement = Main.connection.createStatement();
		statement.execute(query);
	}

	public ResultSet GetAllJourney() throws SQLException {
		String query="SELECT * FROM tblRota";
		Statement statement=Main.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}
	public ResultSet TicketControlForSeat(String seferId) throws SQLException {
		String query="Select  y.koltukNumarasi, y.Cinsiyet from tblSefer s INNER JOIN tblBilet b on s.seferID=b.seferID "
				+" INNER JOIN tblYolcu y on y.yolcuID=b.yolcuID  where s.seferID="+Integer.parseInt(seferId)+"";
		Statement statement = Main.connection.createStatement();
		ResultSet resultset = statement.executeQuery(query);
		return resultset;
	}
	public void InsertTraveler(String Ad,String Soyad,String cinsiyet,String tckn,String KoltukNo) throws SQLException {
		String adsoyad=Ad +" "+ Soyad; 
		String query="INSERT INTO tblYolcu (adSoyad,TCKN,cinsiyet,koltukNumarasi)"
				+ " values('"+adsoyad+"','"+tckn+"','"+cinsiyet+"',"+Integer.parseInt(KoltukNo)+")";
		Statement statement = Main.connection.createStatement();
		statement.execute(query);
	}
	public ResultSet GetTicket(String seferId) throws SQLException {
		String query="SELECT s.kalkisSaati,s.kalkisTarihi,r.Baslangic,r.Bitis,s.seferUcreti"
				+ " FROM  tblSefer s INNER JOIN tblRota r on(s.rotaID=r.rotaID) where"
				+ " s.seferID='"+seferId+"'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultset = statement.executeQuery(query);
		return resultset;
	}
	public ResultSet SelectTraveler(String tckn) throws SQLException {
		String Selectquery="SELECT yolcuID  FROM tblYolcu  where TCKN='"+tckn+"' ";
		Statement statement =Main.connection.createStatement();
		ResultSet  resultset=statement.executeQuery(Selectquery);
		return resultset;
	}

	public void InsertTicket(String yolcuId,String seferId) throws SQLException {
		String query="INSERT INTO tblBilet (yolcuID,seferID) values("+Integer.parseInt(yolcuId)+","+Integer.parseInt(seferId)+")";

		Statement statement = Main.connection.createStatement();
		statement.execute(query);
	}

	public void InsertBus(String Plaka,String Marka) throws SQLException {
		String query="INSERT INTO tblOtobus (otobusMarka,otobusPlaka) values ('"+Marka+"','"+Plaka+"')";
		Statement statement =Main.connection.createStatement();
		statement.execute(query);
	}
	public ResultSet GetAllBus() throws SQLException {
		String query="Select * FROM tblOtobus";
		Statement statement = Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(query);
		return  resultset;
	}

	public ResultSet GetAllActiveBus() throws SQLException {
		String query="Select * FROM tblOtobus WHERE isActive = 1";
		Statement statement = Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(query);
		return  resultset;
	}

	public ResultSet GetAllJourneyByID(String busID) throws SQLException {
		String query = "SELECT S.seferID, S.kalkisSaati, S.kalkisTarihi, R.baslangic, R.bitis,S.seferUcreti "
				+ "FROM tblSefer S INNER JOIN tblRota R ON S.rotaID = R.rotaID WHERE S.otobusID = " + busID;
		Statement statement = Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(query);
		return  resultset;
	}

	public ResultSet GetBiletByTCKN(String TCKN) throws SQLException {
		String query = "SELECT b.biletID,Y.adSoyad, Y.koltukNumarasi, S.kalkisSaati, S.kalkisTarihi, R.Baslangic, R.Bitis, S.seferUcreti "
				+ "FROM tblBilet B INNER JOIN tblYolcu Y ON B.yolcuID = Y.yolcuID "
				+ "INNER JOIN tblSefer S ON B.seferID = S.seferID "
				+ "INNER JOIN tblRota R ON S.rotaID = R.rotaID "
				+ "WHERE Y.TCKN = '" + TCKN +"'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(query);
		return resultset;
	}
	public void DeleteBilet(String biletId) throws SQLException {

		String query="DELETE FROM tblBilet where biletID="+biletId+"";
		Statement statement =Main.connection.createStatement();
		statement.execute(query);

	}
	public void DeleteBusandOthers(String busID) throws SQLException {
		String query = "UPDATE tblOtobus SET isActive="+0+" where otobusID="+busID+"";
		Statement  statement = Main.connection.createStatement();
		statement.execute(query);

		String seferid="";
		String Seferquery="Select seferID From tblSefer where otobusID="+busID+"";

		ResultSet resultset=statement.executeQuery(Seferquery);
		while(resultset.next()) {
			seferid=resultset.getString("seferID");
		}

		String DeleteBilet="DELETE  FROM tblBilet where seferID="+seferid+"";
		statement.execute(DeleteBilet);

		String DeleteSefer = "DELETE  FROM tblSefer where otobusID="+busID+"";
		statement.execute(DeleteSefer);
	}

	public void UpdateJourney(String seferID, String date, String time, String price) throws SQLException {
		String query = "UPDATE tblSefer SET kalkisTarihi = '" + date + "',kalkisSaati = '" 
				+ time + "',seferUcreti = '" + price + "'  WHERE seferID = '" + seferID + "'";
		Statement  statement = Main.connection.createStatement();
		statement.execute(query);
	}


	public ResultSet GetFrom_Journey() throws SQLException {
		String query = "SELECT DISTINCT R.baslangic FROM tblRota R";
		Statement statement = Main.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);

		return resultSet;
	}

	public ResultSet GetTo_Journey(String from) throws SQLException {
		String query = "SELECT DISTINCT R.bitis FROM tblRota R WHERE R.baslangic = '" + from + "'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);

		return resultSet;
	}
	public ResultSet GetBusforRotaId(String rotaId) throws SQLException {
		String Query="SELECT O.otobusMarka,O.otobusPlaka,O.otobusID  FROM"
				+ " tblSefer S INNER JOIN tblRota R on(S.rotaID=R.rotaID) "
				+ " INNER JOIN tblOtobus O on(O.otobusID=S.otobusID)  "
				+ " where R.rotaID='"+rotaId+"'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(Query);
		
		
		return resultset;

	}
}
