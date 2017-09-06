using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication1
{
    public partial class Search_Colleagues : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            People_and_Job_Information_Management pjim = new People_and_Job_Information_Management();

            List<Person> result = pjim.GetColleagues(TextBox1.Text, TextBox2.Text);

            TextBox3.Text = "";

            if (result != null)
            {
                for (int i = 0; i < result.Count; i++)
                {
                    TextBox3.Text = TextBox3.Text + result[i].firstName + " " + result[i].lastName + " " + result[i].dateOfBirth + " " 
                        + result[i].email + " " + result[i].streetAddress + " " + result[i].suburb + " " + result[i].state + " "
                        + result[i].postcode + " " + result[i].job.positionNumber + " " + result[i].job.positionTitle + " "
                        + result[i].job.positionDescription + " " + result[i].job.companyName + "\r\n";
                }     
            }
            else TextBox3.Text = "Not found";
        }
    }
}