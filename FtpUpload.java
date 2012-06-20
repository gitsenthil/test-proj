import ftp.*;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
/**
 * This is a example of using FtpBean.
 * It connects to a ftp server. Go to a directory.
 * Then list its content with help of the FtpListResult class.
 * Finally, it gets a binary file. In the downloading
 * progress, it tells how many bytes are being downloaded.
 *
 * Note that this class implements the FtpObserver interface, which
 * make this class have the ability to monitor the downloading or
 * uploading progress. If you don't need to monitor it, then you
 * don't need to implement this interface.
 *
 * For using more function of this FtpBean, please see the documentation.
 */
class FtpUpload implements FtpObserver
{
    FtpBean ftp;
    FtpListResult ftplrs = null;
    Calendar calen = Calendar.getInstance();
    int m_nWeek = calen.get(Calendar.WEEK_OF_MONTH);
    int nLen = now().length();
    long num_of_bytes = 0;
    String m_strUserName = "";
    String m_strPassword = "";
    String m_strIpAddress = "";
    String m_strSourcePath = "";
    String m_strServerPath = "";
    String m_strDescPath = "";
    String[] m_strMon = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String m_strYear = now().substring((nLen-4), nLen);
    String m_strCurrentDate = now("yyyyMMdd");
    String m_strMonth = now().substring(3, (nLen-5));

    public FtpUpload()
    {
        // Create a new FtpBean object.
        ftp = new FtpBean();
        try
        {
            Properties INI = new Properties();
            INI.load(new FileInputStream("Ftp.ini"));
            m_strUserName = INI.getProperty("UserName");
            m_strPassword = INI.getProperty("Password");
            m_strIpAddress = INI.getProperty("IpAddress");
            m_strSourcePath = INI.getProperty("SourcePath");
            m_strServerPath = INI.getProperty("ServerPath");
            m_strDescPath = INI.getProperty("DescPath");

        } catch( Exception e) {
            System.out.println(e);
        }
    }

    // Connect to a ftp server.
    public void connect()
    {
        try
        {
            ftp.ftpConnect(m_strIpAddress, m_strUserName, m_strPassword);
            System.out.println("Successfully Connected");
        } catch(Exception e)
        {
            System.out.println(e);
        }
    }

    // Close connection
    public void close()
    {
        try
        {
            ftp.close();
            System.out.println("Connection Closed");
        } catch(Exception e)
        {
            System.out.println(e);
        }
    }

    // Go to directory pub and list its content.
    public void listDirectory()
    {
        try
        {
            // Go to directory 'pub/redhat/redhat-6.2/i386/RedHat/RPMS'.
            ftp.setDirectory("");
            // Get its directory content.
            ftplrs = ftp.getDirectoryContent();
        } catch(Exception e)
        {
            System.out.println(e);
        }

        // Print out the type and file name of each row.
        while(ftplrs.next())
        {
            int type = ftplrs.getType();
            if(type == FtpListResult.DIRECTORY)
                System.out.print("DIR\t");
            else if(type == FtpListResult.FILE)
                System.out.print("FILE\t");
            else if(type == FtpListResult.LINK)
                System.out.print("LINK\t");
            else if(type == FtpListResult.OTHERS)
                System.out.print("OTHER\t");
            System.out.println(ftplrs.getName());
        }
    }

    public void CreateYearDirectory()
    {
        try
        {
            boolean bFileExist = false;
            ftplrs = ftp.getDirectoryContent();
            while(ftplrs.next())
            {
                int type = ftplrs.getType();
                if(type == FtpListResult.DIRECTORY)
                {
                    if(m_strYear.equals(ftplrs.getName()))
                        bFileExist = true;
                }
            }
            if(!bFileExist) {
                ftp.makeDirectory(m_strYear);
                bFileExist = false;
            }
        } catch (Exception e) {
            System.out.println("Error in Creating Year Directory"+e);
        }
    }

    public void CreateMonthDirectory()
    {
        try
        {
            boolean bFileExist = false;
            String strMonthDirectory = m_strServerPath + "/" + m_strYear;
            ftp.setDirectory(strMonthDirectory);
            ftplrs = ftp.getDirectoryContent();
            while(ftplrs.next())
            {
                int type = ftplrs.getType();
                System.out.println(ftplrs.getName());
                if(type == FtpListResult.DIRECTORY)
                {
                    if(m_strMonth.equals(ftplrs.getName()))
                        bFileExist = true;
                }
            }
            if(!bFileExist) {
                ftp.makeDirectory(m_strMonth);
                bFileExist = false;
            }
        } catch (Exception e) {
            System.out.println("Error in Creating Year Directory"+e);
        }
    }

    public void CreateWeekDirectory()
    {
        try
        {
            boolean bFileExist = false;
            String strFtpServerpath = m_strYear + "/" + m_strMonth;
            String strWeek = String.valueOf(m_nWeek);

            String strtest = m_strServerPath + "/" + strFtpServerpath;
            ftp.setDirectory(strtest);
            ftplrs = ftp.getDirectoryContent();
            while(ftplrs.next())
            {
                int type = ftplrs.getType();
                System.out.println(ftplrs.getName());
                if(type == FtpListResult.DIRECTORY)
                {
                    if(strWeek.equals(ftplrs.getName()))
                        bFileExist = true;
                }
            }
            if(!bFileExist) {
                ftp.makeDirectory(strWeek);
                bFileExist = false;
            }
        } catch (Exception e) {
            System.out.println("Error in Creating Year Directory"+e);
        }
    }

    public void CreateDateDirectory()
    {

        try
        {
            boolean bFileExist = false;
            String strFtpServerpath = m_strYear + "/" + m_strMonth + "/" + m_nWeek;
            String strDateDirectory = m_strServerPath + "/" + strFtpServerpath;
            ftp.setDirectory(strDateDirectory);
            ftplrs = ftp.getDirectoryContent();
            while(ftplrs.next())
            {
                int type = ftplrs.getType();
                System.out.println(ftplrs.getName());
                if(type == FtpListResult.DIRECTORY)
                {
                    if(m_strCurrentDate.equals(ftplrs.getName()))
                        bFileExist = true;
                }
            }
            if(!bFileExist) {
                ftp.makeDirectory(m_strCurrentDate);
                bFileExist = false;
            }
        } catch (Exception e) {
            System.out.println("Error in Creating Year Directory"+e);
        }
    }

    public static final String DATE_FORMAT_NOW = "dd MMMMM yyyy";

    public String now()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public String now(String DateFormat)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        return sdf.format(cal.getTime());
    }

    public String day()
    {
        //create Calendar instance
        Calendar now = Calendar.getInstance();

        //create an array of days
        String[] strDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thusday", "Friday", "Saturday"};

        //Day_OF_WEEK starts from 1 while array index starts from 0
         return strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public String Month()
    {
        //create Calendar instance
        Calendar now = Calendar.getInstance();

        //create an array of days
        String[] strDays = new String[]{"January", "February", "March", "April", "May", "June", "July","August", "September","October","November","December"};

        //Day_OF_WEEK starts from 1 while array index starts from 0
         return strDays[now.get(Calendar.MONTH) - 1];
    }

    // Get the file.
    public void getFile()
    {
        try
        {
            java.util.Date now1 = new java.util.Date();
            String m_strCurTime = now1.getHours() + "-" + now1.getMinutes() + "-" + now1.getSeconds();
            String strFtpServerpath = m_strYear + "/" + m_strMonth + "/" + m_nWeek + "/" + m_strCurrentDate;

            CreateYearDirectory();
            CreateMonthDirectory();
            CreateWeekDirectory();
            CreateDateDirectory();

            String strSetFtpFinalPath = m_strServerPath+"/"+strFtpServerpath;
            ftp.setDirectory(strSetFtpFinalPath);
            // Get its directory content.
            ftplrs = ftp.getDirectoryContent();

            String strCopyPath = "";
            File fdir;
            try
            {
                fdir = new File(m_strDescPath);
                if (!fdir.exists()) {
                    fdir.mkdirs();
                }

                strCopyPath = m_strDescPath + "\\" + m_strYear;
                fdir = new File(strCopyPath);
                if (!fdir.exists()) {
                    fdir.mkdirs();
                }

                fdir = new File(strCopyPath + "\\" + m_strMonth);
                if (!fdir.exists()) {
                    fdir.mkdirs();
                }

                fdir = new File(strCopyPath + "\\" + m_strMonth + "\\"  + m_nWeek);
                if (!fdir.exists()) {
                    fdir.mkdirs();
                }

                fdir = new File(strCopyPath +  "\\" + m_strMonth + "\\"  + m_nWeek + "\\"  + m_strCurrentDate);
                if (!fdir.exists()) {
                    fdir.mkdirs();
                }
            } catch(Exception e) {
                System.out.println(e);
            }

            //String strFtpFilePath = strYear +  "\\" + strMonth  + "\\"  + nWeek + "\\" + strCurrentDate;
            String strFtpFilePath = strFtpServerpath;
            String strFileName = "Antecura" + "_" + m_strCurrentDate + "_" + m_strCurTime + ".zip";
            String m_strCopyPath = strCopyPath + "\\" + m_strMonth + "\\"  + m_nWeek + "\\"  + m_strCurrentDate + "\\" + strFileName;
            System.out.println("File to Zip :"+strFileName);
            FolderZiper.zipFolder(m_strSourcePath, m_strCopyPath);

            strFtpFilePath = strFileName;
            System.out.println("File to Upload :"+strFileName);
            ftp.putBinaryFile(m_strCopyPath, strFtpFilePath , this);
            System.out.println("Successfully Uploaded");
        } catch(Exception e)
        {
            System.out.println("Error in FTP Upload"+e);
        }
    }

    public void byteRead(int bytes)
    {
        num_of_bytes += bytes;
        System.out.println(num_of_bytes + " of bytes read already.");
    }

    // Needed to implements by FtpObserver interface.
    public void byteWrite(int bytes)
    {
    }

    // Main
    public static void main(String[] args)
    {
        FtpUpload Ftp = new FtpUpload();
        if((!Ftp.day().equals("Sunday")) && (!Ftp.day().equals("Saturday")))
        {
            Ftp.connect();
            //Ftp.listDirectory();
            Ftp.getFile();
            Ftp.close();
        } else {
            System.out.println("Week End");
        }
    }
}
