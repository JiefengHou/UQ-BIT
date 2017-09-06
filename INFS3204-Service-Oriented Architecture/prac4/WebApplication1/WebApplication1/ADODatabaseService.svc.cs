using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

using System.Data;
using System.Data.Sql;
using System.Data.SqlClient;



namespace WebApplication1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "ADODatabaseService" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select ADODatabaseService.svc or ADODatabaseService.svc.cs at the Solution Explorer and start debugging.
    public class ADODatabaseService : IADODatabaseService
    {
        SqlConnection con = new SqlConnection("Data Source=(LocalDB)\\v11.0;AttachDbFilename=F:\\WebApplication1\\WebApplication1\\App_Data\\ADODatabase.mdf;Integrated Security=True");

        public Boolean PatientRegistration(string healthInsuranceNO, string fname, string lname, int pnumber, string address, string email)
        {            
            try
            {
                con.Open();
                string mysql = "INSERT INTO Patient([Health Insurance NO.],[First name],[Last name],[Phone number],Address,Email) VALUES(@healthInsuranceNO,@fname,@lname,@pnumber,@address,@email)";

                SqlCommand mycmd = new SqlCommand(mysql, con);


                mycmd.Parameters.AddWithValue("@healthInsuranceNO", healthInsuranceNO);

                mycmd.Parameters.AddWithValue("@fname", fname);

                mycmd.Parameters.AddWithValue("@lname", lname);

                mycmd.Parameters.AddWithValue("@pnumber", pnumber);
                
                mycmd.Parameters.AddWithValue("@address", address);

                if (email== null)
                {
                    mycmd.Parameters.AddWithValue("@email", DBNull.Value);
                }
                else
                {
                    mycmd.Parameters.AddWithValue("@email", email);
                } 

                int result = mycmd.ExecuteNonQuery();
                if (result == 1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
            finally
            {
                con.Close();
            }
            
        }

        public string[] GetPatientInfo(string fname, string lname)
        { 
            try
            {
                if (fname == null)
                {

                }
                con.Open();
                string mysql = "SELECT [Health Insurance NO.],[Phone number],Address,Email FROM Patient WHERE [First name]=@fname AND [Last name]=@lname";
                SqlCommand mycmd = new SqlCommand(mysql, con);

                mycmd.Parameters.AddWithValue("@fname", fname);
                mycmd.Parameters.AddWithValue("@lname", lname);

                SqlDataReader reader = mycmd.ExecuteReader();
                if (reader.Read())
                {
                    string[] result = new string[4];
                    result[0] = reader["Health Insurance NO."].ToString();
                    result[1] = reader["Phone number"].ToString();
                    result[2] = reader["Address"].ToString();
                    result[3] = reader["Email"].ToString();
        
                    return result;
                }

                return null;

            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
            finally
            {
                con.Close();
            }
        }

        public Boolean DoctorRegistration(string medicalRegistrationNO, string fname, string lname, string HealthProfession, int pnumber, string email)
        {
            try
            {
                con.Open();
                string mysql = "INSERT INTO Doctor([Medical Registration NO.],[First name],[Last name],[Health Profession],[Phone number],Email) VALUES(@medicalRegistrationNO,@fname,@lname,@HealthProfession,@pnumber,@email)";

                SqlCommand mycmd = new SqlCommand(mysql, con);
                mycmd.Parameters.AddWithValue("@medicalRegistrationNO", medicalRegistrationNO);
                mycmd.Parameters.AddWithValue("@fname", fname);
                mycmd.Parameters.AddWithValue("@lname", lname);
                mycmd.Parameters.AddWithValue("@HealthProfession", HealthProfession);
                mycmd.Parameters.AddWithValue("@pnumber", pnumber);

                if (string.IsNullOrWhiteSpace(email))
                {                    
                    mycmd.Parameters.AddWithValue("@email", DBNull.Value);
                }
                else
                {
                    mycmd.Parameters.AddWithValue("@email", email);
                } 

                int result = mycmd.ExecuteNonQuery();
                if (result == 1)
                {
                    return true;
                }
                else
                {
                    return false;
                }


            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet GetDoctorInfo(string fname, string lname)
        {
            try
            {
                con.Open();
                string mysql = "SELECT [Medical Registration NO.],[Health Profession],[Phone number],Email FROM Doctor WHERE [First name]=@fname AND [Last name]=@lname";
                SqlDataAdapter myadapter = new SqlDataAdapter();
                DataSet ds = new DataSet();

                myadapter.SelectCommand = new SqlCommand(mysql, con);
                myadapter.SelectCommand.Parameters.AddWithValue("@fname", fname);
                myadapter.SelectCommand.Parameters.AddWithValue("@lname", lname);

                myadapter.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    return ds;
                }

                return null;

            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
            finally
            {
                con.Close();
            }
        }

        public Boolean AppointmentBooking(string pfname, string plname, string dfname, string dlname, DateTime appointmentDateTime, string cname)
        {
            string healthInsuranceNO;
            string medicalRegistrationNO;
            
            try
            {
                if (GetPatientInfo(pfname, plname) == null)
                {
                    throw new Exception("Patient's information is not found.");
                }
                else
                {
                    healthInsuranceNO = GetPatientInfo(pfname, plname)[0];
                }

                if (GetDoctorInfo(dfname, dlname) == null)
                {
                    throw new Exception("Doctor's information is not found.");
                }
                else
                {
                    medicalRegistrationNO = GetDoctorInfo(dfname, dlname).Tables[0].Rows[0]["Medical Registration NO."].ToString();
                }

                con.Open();
                string mysql = "INSERT INTO Appointment([Health Insurance NO.],[Medical Registration NO.],[Appointment date & time],[Clinic name]) VALUES(@healthInsuranceNO,@medicalRegistrationNO,@appointmentDateTime,@cname)";

                SqlCommand mycmd = new SqlCommand(mysql, con);


      
                    mycmd.Parameters.AddWithValue("@healthInsuranceNO", healthInsuranceNO);
               

                    mycmd.Parameters.AddWithValue("@medicalRegistrationNO", medicalRegistrationNO);
                
               
                mycmd.Parameters.AddWithValue("@appointmentDateTime", appointmentDateTime);
                mycmd.Parameters.AddWithValue("@cname", cname);

                int result = mycmd.ExecuteNonQuery();
                if (result == 1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
            finally
            {
                con.Close();
            }
           
            
        }

        public DataSet GetAppointment(string pfname, string plname, string dfname, string dlname)
        {
            string healthInsuranceNO;
            string medicalRegistrationNO;

            try
            {
                if (GetPatientInfo(pfname, plname) == null)
                {
                    throw new Exception("Patient's information is not found.");
                }
                else
                {
                    healthInsuranceNO = GetPatientInfo(pfname, plname)[0];
                }

                if (GetDoctorInfo(dfname, dlname) == null)
                {
                    throw new Exception("Doctor's information is not found.");
                }
                else
                {
                    medicalRegistrationNO = GetDoctorInfo(dfname, dlname).Tables[0].Rows[0]["Medical Registration NO."].ToString();
                }

                con.Open();
                string mysql = "SELECT [Appointment date & time],[Clinic name] FROM Appointment WHERE [Health Insurance NO.]=@healthInsuranceNO AND [Medical Registration NO.]=@medicalRegistrationNO";
                SqlDataAdapter myadapter = new SqlDataAdapter();
                DataSet ds = new DataSet();

                myadapter.SelectCommand = new SqlCommand(mysql, con);
                myadapter.SelectCommand.Parameters.AddWithValue("@healthInsuranceNO", healthInsuranceNO);
                myadapter.SelectCommand.Parameters.AddWithValue("@medicalRegistrationNO", medicalRegistrationNO);

                myadapter.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    return ds;
                }

                return null;

            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
            finally
            {
                con.Close();
            }
        }

        public Boolean AppointmentReschedule(string pfname, string plname, string dfname, string dlname, DateTime appointmentDateTime)
        {
            try
            {
                string healthInsuranceNO = GetPatientInfo(pfname, plname)[0];
                string medicalRegistrationNO = GetDoctorInfo(dfname, dlname).Tables[0].Rows[0]["Medical Registration NO."].ToString();
                
                con.Open();
                string mysql = "SELECT * FROM Appointment WHERE [Health Insurance NO.]=@healthInsuranceNO AND [Medical Registration NO.]=@medicalRegistrationNO";
                SqlDataAdapter myadapter = new SqlDataAdapter();
                SqlCommandBuilder cb = new SqlCommandBuilder(myadapter);
                DataSet ds = new DataSet();

                myadapter.SelectCommand = new SqlCommand(mysql, con);
                myadapter.SelectCommand.Parameters.AddWithValue("@healthInsuranceNO", healthInsuranceNO);
                myadapter.SelectCommand.Parameters.AddWithValue("@medicalRegistrationNO", medicalRegistrationNO);

                myadapter.Fill(ds);

                ds.Tables[0].Rows[0]["Appointment date & time"] = appointmentDateTime;

                myadapter.Update(ds);

                return true;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
            finally
            {
                con.Close();
            }
           
            
        }



    }
}
