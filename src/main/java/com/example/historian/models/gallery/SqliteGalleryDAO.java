package com.example.historian.models.gallery;

import com.example.historian.models.account.Password;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.utils.SqliteConnection;

import java.sql.*;
import java.util.List;
import java.util.Date;

public class SqliteGalleryDAO implements IGalleryDAO {
    private final Connection connection;

    public SqliteGalleryDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    @Override
    public String addGallery(String title, Date fromDate, Date toDate, int location, int person) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO galleries (title, from_date, to_date, locationId, personId) VALUES (?, ?, ?, ?, ?)");

            statement.setString(1, title);
            if (fromDate != null) {
                statement.setLong(2, fromDate.getTime());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            if (toDate != null) {
                statement.setLong(3, toDate.getTime());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            if (location != -1) {
                statement.setInt(4, location);
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            if (person != -1) {
                statement.setInt(5, person);
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int galleryId = generatedKeys.getInt(1);
                Password encodedKey = new Password(String.valueOf(galleryId));
                String publicKey = encodedKey.getHash().substring(0, 6);

                statement = connection.prepareStatement("UPDATE galleries SET key = ? WHERE id = ?");
                statement.setString(1, publicKey);
                statement.setInt(2, galleryId);
                statement.executeUpdate();

                return publicKey;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS galleries ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title VARCHAR NOT NULL,"
                    + "key VARCHAR,"
                    + "from_date INTEGER,"
                    + "to_date INTEGER,"
                    + "locationId VARCHAR,"
                    + "personId VARCHAR,"
                    + "FOREIGN KEY(locationId) REFERENCES locations(id),"
                    + "FOREIGN KEY(personId) REFERENCES people(id))";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Gallery createFromResultSet(ResultSet resultSet) throws Exception {

        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");

        long fromDateLong = resultSet.getObject("from_date") != null ? resultSet.getLong("from_date") : -1;
        Date fromDate = fromDateLong != -1 ? new Date(fromDateLong) : null;
        long toDateLong = resultSet.getObject("to_date") != null ? resultSet.getLong("to_date") : -1;
        Date toDate = toDateLong != -1 ? new Date(toDateLong) : null;
        int locationId = resultSet.getObject("locationId") != null ? resultSet.getInt("locationId") : -1;
        int personId = resultSet.getObject("personId") != null ? resultSet.getInt("personId") : -1;

        IPhotoDAO photoDAO = new SqlitePhotoDAO();
        List<Photo> photos =  photoDAO.getPhotosByFilter(fromDate, toDate, locationId, personId);
        Gallery gallery = new Gallery(title, photos);
        gallery.setId(id);
        return gallery;
    }

    @Override
    public Gallery getGalleryByKey(String galleryKey) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM galleries WHERE key = ?");
            statement.setString(1, galleryKey);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createFromResultSet(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean checkIfGalleryExistsByKey(String galleryKey) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM galleries WHERE key = ?");
            statement.setString(1, galleryKey);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
