﻿<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Doctor Registration.aspx.cs" Inherits="WebApplication1.Doctor_Registration" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <style type="text/css">
                
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
           width: 1129px;
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

       .auto-style6 {
         width: 100%;
     }

     .auto-style7 {
         width: 215px;
     }

    </style>
</head>
<body>
    <div>
       <div id="head"><h1><span class="style">M</span>Y <span class="style">ASP.NET A</span>PPLICATION</h1></div>
       <div id="navigation">
           <ul>
               <li><asp:Hyperlink ID="Hyperlink1" runat="server" NavigateUrl="Patient Registration.aspx">Save Information Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink2" runat="server" NavigateUrl="Doctor Registration.aspx">Search Person’s Job Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink3" runat="server" NavigateUrl="Appointment Booking.aspx">Search Colleagues Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink4" runat="server" NavigateUrl="Appointment Rescheduling.aspx">Search Colleagues Page</asp:Hyperlink></li>
           </ul>
       </div>
    </div>

    <div id="title">
        <h3>Doctor Registration Page:</h3>
    </div>
    <form id="form1" runat="server">

    <div>
    
        <table class="auto-style6">
            <tr>
                <td class="auto-style7">
                    <asp:Label ID="Label1" runat="server" Text="Medical Registration NO."></asp:Label>
                </td>
                <td>
                    <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
                </td>
            </tr>
            <tr>
                <td class="auto-style7">
                    <asp:Label ID="Label2" runat="server" Text="First name"></asp:Label>
                </td>
                <td>
                    <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
                </td>
            </tr>
            <tr>
                <td class="auto-style7">
                    <asp:Label ID="Label3" runat="server" Text="Last name"></asp:Label>
                </td>
                <td>
                    <asp:TextBox ID="TextBox3" runat="server"></asp:TextBox>
                </td>
            </tr>
            <tr>
                <td class="auto-style7">
                    <asp:Label ID="Label4" runat="server" Text="Health Profession"></asp:Label>
                </td>
                <td>
                    <asp:DropDownList ID="DropDownList1" runat="server">
                        <asp:ListItem></asp:ListItem>
                        <asp:ListItem>Chiropractor</asp:ListItem>
                        <asp:ListItem>Dental Practitioner</asp:ListItem>
                        <asp:ListItem>Medical Practitioner</asp:ListItem>
                        <asp:ListItem>Optometrist</asp:ListItem>
                        <asp:ListItem>Osteopath</asp:ListItem>
                        <asp:ListItem>Physiotherapist</asp:ListItem>
                        <asp:ListItem>Psychologist</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="auto-style7">
                    <asp:Label ID="Label5" runat="server" Text="Phone number"></asp:Label>
                </td>
                <td>
                    <asp:TextBox ID="TextBox4" runat="server"></asp:TextBox>
                </td>
            </tr>
            <tr>
                <td class="auto-style7">
                    <asp:Label ID="Label6" runat="server" Text="Email"></asp:Label>
                </td>
                <td>
                    <asp:TextBox ID="TextBox5" runat="server"></asp:TextBox>
                </td>
            </tr>
            <tr>
                <td class="auto-style7">
                    <asp:Button ID="Button1" runat="server" Text="Save" OnClick="Button1_Click" />
                    <asp:Button ID="Button2" runat="server" Text="Search" OnClick="Button2_Click" />
                </td>
                <td>
                    <asp:Label ID="Label7" runat="server"></asp:Label>
                </td>
            </tr>
        </table>
    
    </div>
    </form>
</body>
</html>
