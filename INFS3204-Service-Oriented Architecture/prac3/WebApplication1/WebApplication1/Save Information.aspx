<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Save Information.aspx.cs" Inherits="WebApplication1.Save_Information" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1" runat="server">
    <title></title>
    <style type="text/css">
        .auto-style1 {
            width: 100%;
        }
        .auto-style2 {
            width: 182px;
        }
        .auto-style3 {
            width: 185px;
        }
        .auto-style4 {
            width: 210px;
        }
        .auto-style5 {
            height: 29px;
        }

        #head {
            width:100%; 
            color:#ffffff;
            background-color:#336699;  
            font-family:'Times New Roman';
            width:100%;
            float:left;
            height:auto;
            
        }

        h1 {
            font-size:28px;
            font-weight:bold;
            float:left;
            margin:15px 10px;

        }

        .style {
            font-size:120%;
            font-weight:bolder;
        }

        #navigation {
            background-color:#666699;
            color:#E8E8E8;
            list-style: none;
            float:left;
            width:100%;
            
        }

        ul {
           float:left;
           margin:5px -30px;
        }

        li {
            display:inline;
            float:left;
            border-style:solid;
            border-width:1px;
            border-color:#E8E8E8;
            padding:5px 20px;
            
        }

        #title {
            float:left;
            width:100%;
            height:100%;
            font-size:150%;
            font-weight:bolder;
        }

    </style>
</head>
<body>
    <div>
       <div id="head"><h1><span class="style">M</span>Y <span class="style">ASP.NET A</span>PPLICATION</h1></div>
       <div id="navigation">
           <ul>
               <li><asp:Hyperlink ID="Hyperlink1" runat="server" NavigateUrl="Save Information.aspx">Save Information Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink2" runat="server" NavigateUrl="Search Person’s Job.aspx">Search Person’s Job Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink3" runat="server" NavigateUrl="Search Colleagues.aspx">Search Colleagues Page</asp:Hyperlink></li>
           </ul>
       </div>
    </div>

    <div id="title">
        <h3>Save Information Page:</h3>
    </div>

    

    <form id="form1" runat="server">
    <div>
 
        <asp:Label ID="Label1" runat="server" Text="Person Information" Font-Bold="True"></asp:Label>
        <br />
        <br />
        <table class="auto-style1">
            <tr>
                <td>
                    <asp:Label ID="Label2" runat="server" Text="First Name"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label3" runat="server" Text="Last Name"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label4" runat="server" Text="Date of Birth"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label5" runat="server" Text="Email"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label6" runat="server" Text="Address"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label7" runat="server" Text="Suburb"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label8" runat="server" Text="State"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label9" runat="server" Text="Postcode"></asp:Label>
                </td>
            </tr>
            <tr>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox3" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox4" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox5" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox6" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox7" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style5">
                    <asp:TextBox ID="TextBox8" runat="server"></asp:TextBox>
                </td>
            </tr>
        </table>

        <br />
        <asp:Label ID="Label10" runat="server" Text="Job Information" Font-Bold="True"></asp:Label>
        <br />
        <br />
        <table class="auto-style1">
            <tr>
                 <td class="auto-style2">
                    <asp:Label ID="Label11" runat="server" Text="Position Number"></asp:Label>
                </td>
                <td class="auto-style3">
                    <asp:Label ID="Label12" runat="server" Text="Position Title"></asp:Label>
                </td>
                <td class="auto-style4">
                    <asp:Label ID="Label13" runat="server" Text="Position Description"></asp:Label>
                </td>
                <td>
                    <asp:Label ID="Label14" runat="server" Text="Company Name"></asp:Label>
                </td>
            </tr>
            <tr>
                <td class="auto-style2">
                    <asp:TextBox ID="TextBox9" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style3">
                    <asp:TextBox ID="TextBox10" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style4">
                    <asp:TextBox ID="TextBox11" runat="server"></asp:TextBox>
                </td>
                <td>
                    <asp:TextBox ID="TextBox12" runat="server"></asp:TextBox>
                </td>
            </tr>
        </table>
        <br />
        <asp:Button ID="Button1" runat="server" Text="Save" OnClick="Button1_Click" />     
        &nbsp;&nbsp;&nbsp;
        <asp:Label ID="Label15" runat="server"></asp:Label>
        <br />
    </div>
    </form>
</body>
</html>
