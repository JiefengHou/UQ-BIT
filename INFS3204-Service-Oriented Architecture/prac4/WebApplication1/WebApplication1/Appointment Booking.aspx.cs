using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication1
{
    public partial class Appointment_Booking : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            try
            {
                
                DateTime dt;
                string dateTime = TextBox5.Text + " " + TextBox6.Text + ":00";

                if (string.IsNullOrWhiteSpace(TextBox1.Text) || string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text) || string.IsNullOrWhiteSpace(TextBox4.Text) || string.IsNullOrWhiteSpace(TextBox5.Text)
                    || string.IsNullOrWhiteSpace(TextBox6.Text) || string.IsNullOrWhiteSpace(TextBox7.Text))
                {
                    throw new Exception("Patient's name, Doctor's name, Appointment Date, Appointment Time and Clinic Name cannot be empty");
                }
                else if (!DateTime.TryParse(dateTime, out dt))
                {
                    throw new Exception("Appointment date format should be: yyyy-MM-dd and appointment time format should be: HH:mm.");
                }               
                
                
                ADODatabaseService adodbs = new ADODatabaseService();
                

                Boolean result = adodbs.AppointmentBooking(TextBox1.Text, TextBox2.Text, TextBox3.Text, TextBox4.Text, Convert.ToDateTime(dateTime), TextBox7.Text);
                if (result == true)
                {
                    Label8.Text = "Saved successfully";
                }
                else
                {
                    Label8.Text = "Saved failed";
                }
            }
            catch (Exception ex)
            {
                Label8.Text = ex.Message;
            }
        }
    }
}