import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Main {
    public static void main(String[] args) throws IOException {
        try {
            int max = 1000000;
            Connection connection = DriverManager.getConnection("jdbc:mysql://flossdata.syr.edu:3306", "username", "password");
            // Creates file
            BufferedWriter buffW = new BufferedWriter(Create_file());
            Statement stmt = (Statement) connection.createStatement();

            // Get projects from 2005
            String sql = "SELECT DISTINCT proj_unixname FROM sourceforge.sf_project_status";
            sql += " WHERE date_collected > '2005-01-01' "
                    + "AND date_collected < '2005-12-12' "
                    + "AND description != 'Inactive' LIMIT 10000;";
            ResultSet projectname = stmt.executeQuery(sql);
            System.out.println("Created project list!");

            if (projectname != null) {
                // Get number of projects
                projectname.last();
                int count = projectname.getRow();
                // Set project list to first position
                projectname.beforeFirst();
                int projectcount = 0;
                // Runs through all projects
                while (projectname.next()) {
                    projectcount++;
                    if (projectcount==2000)
                        break;
                    System.out.println("Skip " + projectcount + " out of " + count);
                }

                while (projectname.next()) {
                    projectcount++;
                    System.out.println("Processing project " + projectcount + " out of " + count);
                    projectname.getString(1);

                    // Writes project details in file
                    buffW.newLine();
                    buffW.write("{\"index\":{\"_index\":\"projects\", \"_type\":\"project\", \"_id\": \"" + projectcount + "\"}}");
                    buffW.newLine();
                    buffW.write(read_attr(projectname.getString(1), connection));buffW.newLine();

                    //Breaks if max is reached
                    if (projectcount==max)
                        break;
                }
            }
            //Closes all connections
            buffW.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String read_attr(String projectname, Connection connection) {
        try {
            String result = "{\"Name\":\"" + projectname+ "\", ";
            Statement stmt = (Statement) connection.createStatement();

            //License type
            ResultSet a1 = stmt.executeQuery("SELECT sf_project_licenses.code FROM sourceforge.sf_project_licenses WHERE sourceforge.sf_project_licenses.proj_unixname = '" + projectname + "' LIMIT 1;");
            if (a1.next()) {
                result = result + "\"License\":"+Integer.toString(a1.getInt(1));
            }
            else {
                result = result + "\"License\":-1";
            }

            //Operating system
            ResultSet a2 = stmt.executeQuery("SELECT sf_project_operating_system.code FROM sourceforge.sf_project_operating_system WHERE sourceforge.sf_project_operating_system.proj_unixname = '" + projectname + "' LIMIT 1;");
            if (a2.next()) {
                result = result + ", \"Operating_system\":" + Integer.toString(a2.getInt(1));
            }
            else {
                result = result + ", \"Operating_system\":-1";
            }

            //Programming language
            ResultSet a3 = stmt.executeQuery("SELECT sf_project_programming_language.code FROM sourceforge.sf_project_programming_language WHERE sourceforge.sf_project_programming_language.proj_unixname = '" + projectname + "' LIMIT 1;");
            if (a3.next()) {
                result = result + ", \"Programming_language\":" + Integer.toString(a3.getInt(1));
            }
            else {
                result = result + ", \"Programming_language\":-1";
            }

            //Number of developer
            ResultSet a4 = stmt.executeQuery("SELECT sum(dev_count)/count(dev_count) FROM sourceforge.sf_projects WHERE proj_unixname= '" + projectname + "' AND date_collected LIKE '2005%'");
            if (a4.next()) {
                result = result + ", \"NumberofDevs\":" + a4.getInt(1);
            } else {
                result = result + ", \"NumberofDevs\":-1";
            }

            //Downloads
            ResultSet a6 = stmt.executeQuery("SELECT SUM(sf_project_all_time_stats.downloads) FROM sourceforge.sf_project_all_time_stats WHERE sourceforge.sf_project_all_time_stats.proj_unixname= '" + projectname + "' AND sourceforge.sf_project_all_time_stats.data_for_date LIKE '%2005';");
            if (a6.next()) {
                //if(a6.getDouble(1)==0)
                //    return " ";
                result = result + ", \"Downloads\":" + a6.getDouble(1);
            } else {
                result = result + ", \"Downloads\":-1";
            }

            //Webhits
            ResultSet a7 = stmt.executeQuery("SELECT SUM(project_web_hits) FROM sourceforge.sf_project_all_time_stats WHERE proj_unixname='"+projectname+"'  AND data_for_date like'%2005';");
            if (a7.next()) {
                result = result + ", \"Webhits\":" + a7.getInt(1);
            } else {
                result = result + ", \"Webhits\":-1";
            }

            result = result + "}";
            return result;
        } catch (SQLException e) {
            return e.getMessage();
        }
    }
    public static FileWriter Create_file() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd'_'HH-mm-ss");
        Date currentTime = new Date();
        File newFile = new File("/Users/Janis/Output" + formatter.format(currentTime) + ".json");
        newFile.createNewFile();
        FileWriter fileW = new FileWriter(newFile);
        return fileW;
    }
}
