<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Color Mixer.aspx.cs" Inherits="Color_Mixer.Color_Mixer1" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
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
        }

        h3 {
            text-transform:uppercase;
            color:#663333;
            font-weight:normal;
            float:left;
            margin-left:10px;
        }

        .table-style {
            float:left;
            width:100%;
            color:#663333;
        }

        td {
            float:left;
            margin-top:10px;
            margin-left:10px;
        }
        .auto-style1 {
            width: 100%;
        }
    </style>
    <title></title>
</head>
<body>
    <div>
       <div id="head"><h1><span class="style">M</span>Y <span class="style">ASP.NET A</span>PPLICATION</h1></div>
       <div id="navigation">
           <ul>
               <li>Home</li>
               <li>About</li>
           </ul>
       </div>
    </div>
    <div id="title">
        <h3><span class="style">W</span>elcome to <span class="style">C</span>olor <span class="style">M</span>ixer<span class="style">!</span></h3>
    </div>

    <form id="form1" runat="server">
        <div>
    
           
    
            <table class="auto-style1">
                <tr>
                    <td>
                        <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
                    </td>
                    <td>
                        <asp:Label ID="Label1" runat="server" Text="+"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
                    </td>
                    <td>
                        <asp:Button ID="Button1" runat="server" Text="Mix Colors" onclick="Button1_click"/>
                    </td>
                    <td>
                        <asp:Label ID="Label2" runat="server" CssClass="style"></asp:Label>
                    </td>
                </tr>
            </table>
    
           
    
        </div>
    </form>
</body>
</html>
