package managers;

import models.*;

import java.sql.*;

import static java.lang.Math.max;

public class WorkDataBase {
    public void clearDragonTable() {
        String query = "TRUNCATE TABLE City CASCADE";
        String resetSequenceQuery = "ALTER SEQUENCE dragon_id_seq RESTART WITH 1";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(query);
            statement.execute(resetSequenceQuery);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public void deleteDragonById(long id) {
        String query = "DELETE FROM Dragons WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            int rowsAffected = statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDragon(Dragon  dragon){
        String query = "INSERT INTO Dragons (name, coordinates, creationDate, age, weight, type, character, head) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dragon.getName());
            statement.setString(2, dragon.getCoordinates().toString());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(dragon.getCreationDate()));
            statement.setInt(4, dragon.getAge());
            statement.setLong(5, dragon.getWeight());
            statement.setString(6, dragon.getType().toString());
            statement.setString(7, dragon.getCharacter().toString());
            statement.setFloat(8, ((Number) dragon.getHead().getSize()).floatValue());


            statement.executeUpdate();

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // SQLState for unique constraint violation
                Server.logger.info("Ошибка: Пользователь с таким именем уже существует.");
            } else {
                e.printStackTrace();
            }
        }
    }
    public void updateDragonById(Dragon dragon) {
        String query = "UPDATE Dragons SET name = ?, coordinates = ?, creationDate = ?, age = ?, weight = ?, type = ?, character = ?, head = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, dragon.getName());
            statement.setString(2, dragon.getCoordinates().toString());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(dragon.getCreationDate()));
            statement.setInt(4, dragon.getAge());
            statement.setLong(5, dragon.getWeight());
            statement.setString(6, dragon.getType().toString());
            statement.setString(7, dragon.getCharacter().toString());
            statement.setFloat(8, ((Number) dragon.getHead().getSize()).floatValue());
            statement.setLong(9, dragon.getId());

            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка");

        }
    }
    public void addUser(String username, String password, String salt) {
        String query = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, salt);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public void getAllUsers() {


        String query = "SELECT * FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                CollectionManager.user_id_max=max(CollectionManager.user_id_max,id);
                Users user = new Users(name, password, id, salt);
                CollectionManager.users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getAllDragons() {

        String query = "SELECT * FROM dragons";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String coordinatesString = resultSet.getString("coordinates");
                java.time.LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
                Integer age = resultSet.getInt("age");
                long weight = resultSet.getLong("weight");
                String type = resultSet.getString("type");
                String character = resultSet.getString("character");
                Float size = resultSet.getFloat("head");
                Coordinates coordinates = convertStringToCoordinates(coordinatesString);

                DragonHead head = new DragonHead(size);
                DragonType dragonType = DragonType.valueOf(type);
                DragonCharacter dragonCharacter = DragonCharacter.valueOf(character);
                Dragon dragon = new Dragon(id, name, coordinates, creationDate, age, weight, dragonType, dragonCharacter, head);
                CollectionManager.dragon_id_max=max(CollectionManager.dragon_id_max,id);
                // Добавление объекта City в список
                CollectionManager.dragons.add(dragon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private Coordinates convertStringToCoordinates(String coordinatesString) {

        String[] parts = coordinatesString.split(", ");
        long x = Long.parseLong(parts[0]);
        Integer y = Integer.parseInt(parts[1]);
        return new Coordinates(x, y);
    }
}