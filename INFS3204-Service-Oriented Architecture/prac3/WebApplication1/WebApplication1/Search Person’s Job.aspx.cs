using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication1
{
    public partial class Search_Person_s_Job : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            People_and_Job_Information_Management pjim = new People_and_Job_Information_Management();

            Job job = pjim.GetJobInfo(TextBox1.Text, TextBox2.Text);

            if (job != null)
            {
                Label4.Text = "Job Information: "+job.positionNumber.ToString() + " " + job.positionTitle + " " + job.positionDescription + " " + job.companyName;

            }

            else Label4.Text = "Not found";
        }
    }
}