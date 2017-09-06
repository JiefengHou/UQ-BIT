using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication2
{
    public partial class Appointment_Rescheduling : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            Label8.Text = "";

            try
            {

                if (string.IsNullOrWhiteSpace(TextBox1.Text) || string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text) || string.IsNullOrWhiteSpace(TextBox4.Text))
                {
                    throw new Exception("Patient's name and Doctor's name cannot be empty");
                }

                LINQDatabaseService linqdbs = new LINQDatabaseService();
                Appointment result = linqdbs.GetAppointment(TextBox1.Text, TextBox2.Text, TextBox3.Text, TextBox4.Text);

                string[] dateTime = result.Appointment_date___time.ToString().Split(' ');
                TextBox5.Text = dateTime[0];
                TextBox6.Text = dateTime[1];
                TextBox7.Text = result.Clinic_name;

                TextBox1.Enabled = false;
                TextBox2.Enabled = false;
                TextBox3.Enabled = false;
                TextBox4.Enabled = false;
                TextBox7.Enabled = false;
               
            }
            catch (Exception ex)
            {
                Label8.Text = ex.Message;
            }
        }

        protected void Button2_Click(object sender, EventArgs e)
        {
            Label8.Text = "";
            DateTime dt;
            string dateTime = TextBox5.Text + " " + TextBox6.Text;

            try
            {

                if (string.IsNullOrWhiteSpace(TextBox5.Text) || string.IsNullOrWhiteSpace(TextBox6.Text))
                {
                    throw new Exception("Appointment Date and Appointment Time cannot be empty");
                }
                else if (!DateTime.TryParse(dateTime, out dt))
                {
                    throw new Exception("Appointment date format should be: yyyy/MM/dd and appointment time format should be: HH:mm:ss.");
                }

                LINQDatabaseService linqdbs = new LINQDatabaseService();
                Boolean result = linqdbs.AppointmentReschedule(TextBox1.Text, TextBox2.Text, TextBox3.Text, TextBox4.Text, Convert.ToDateTime(dateTime));
                if (result == true)
                {
                    Label8.Text = "Updated successfully";
                }
            }
            catch (Exception ex)
            {
                Label8.Text = ex.Message;
            }
        }
    }
}