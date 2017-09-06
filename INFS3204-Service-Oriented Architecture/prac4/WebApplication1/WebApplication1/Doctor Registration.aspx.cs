using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using System.Data;
using System.Data.Sql;
using System.Data.SqlClient;


namespace WebApplication1
{
    public partial class Doctor_Registration : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            Label7.Text = "";
            try
            {

                int n;
                if (string.IsNullOrWhiteSpace(TextBox1.Text) || string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text) || string.IsNullOrWhiteSpace(DropDownList1.SelectedValue) || string.IsNullOrWhiteSpace(TextBox4.Text))
                {
                    throw new Exception("Health Insurance NO.,First name,Last name,Health Profession,Phone number cannot be empty");
                }
                else if (!int.TryParse(TextBox4.Text, out n))
                {
                    throw new Exception("Phone number shoule be an integer");
                }

                ADODatabaseService adodbs = new ADODatabaseService();
                Boolean result = adodbs.DoctorRegistration(TextBox1.Text, TextBox2.Text, TextBox3.Text, DropDownList1.SelectedValue, Convert.ToInt32(TextBox4.Text), TextBox5.Text);
                if (result == true)
                {
                    Label7.Text = "Saved successfully";
                }
                else
                {
                    Label7.Text = "Saved failed";
                }
            }
            catch (Exception ex)
            {
                Label7.Text = ex.Message;
            }

        }

        protected void Button2_Click(object sender, EventArgs e)
        {
            Label7.Text = "";
            try
            {
                if (string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text))
                {
                    throw new Exception("First name,Last name cannot be empty");
                }

                ADODatabaseService adodbs = new ADODatabaseService();
                DataSet result = adodbs.GetDoctorInfo(TextBox2.Text, TextBox3.Text);
                if (result != null)
                {
                    TextBox1.Text = result.Tables[0].Rows[0]["Medical Registration NO."].ToString();
                    DropDownList1.SelectedValue = result.Tables[0].Rows[0]["Health Profession"].ToString();
                    TextBox4.Text = result.Tables[0].Rows[0]["Phone number"].ToString();
                    TextBox5.Text = result.Tables[0].Rows[0]["Email"].ToString();
                }
                else
                {
                    TextBox1.Text = "";
                    DropDownList1.SelectedValue = "";
                    TextBox4.Text = "";
                    TextBox5.Text = "";
                    Label7.Text = "Not found";
                }
            }
            catch (Exception ex)
            {
                Label7.Text = ex.Message;
            }
        }
    }
}