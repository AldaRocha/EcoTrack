using Microsoft.EntityFrameworkCore;
using Servicios.Models;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddSwaggerGen();

var connectionString = builder.Configuration.GetConnectionString("ConexionSQL");
builder.Services.AddDbContext<EcoTrackContext>(options => options.UseSqlServer(connectionString));
builder.Services.AddCors(options =>
{
    options.AddPolicy("Politicas", app =>
    {
        app.AllowAnyOrigin()
        .AllowAnyHeader()
        .AllowAnyMethod();
    });
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors("Politicas");

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
