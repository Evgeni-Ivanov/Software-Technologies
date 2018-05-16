﻿using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;
using AnimeList.Models;

namespace AnimeList.Controllers
{
    [ValidateInput(false)]
    public class AnimeController : Controller
    {
        [HttpGet]
        [Route("")]
        public ActionResult Index()
        {
            using (AnimeListDbContext db = new AnimeListDbContext())
            {
                List<Anime> animes = db.Animes.ToList();

                return this.View(animes);
            }
        }

        [HttpGet]
        [Route("create")]
        public ActionResult Create()
        {
            return View();
        }

        [HttpPost]
        [Route("create")]
        [ValidateAntiForgeryToken]
        public ActionResult Create(Anime anime)
        {
            if (ModelState.IsValid)
            {
                using (AnimeListDbContext db = new AnimeListDbContext())
                {
                    db.Animes.Add(anime);
                    db.SaveChanges();
                    return Redirect("/");
                }
            }
            return View();
        }

        [HttpGet]
        [Route("delete/{id}")]
        public ActionResult Delete(int? id)
        {
            using (AnimeListDbContext db = new AnimeListDbContext())
            {
                Anime anime = db.Animes.Find(id);

                if (anime == null)
                {
                    return RedirectToAction("Index");
                }

                return View(anime);
            }
        }

        [HttpPost]
        [Route("delete/{id}")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirm(int? id, Anime animeModel)
        {
            using (AnimeListDbContext db = new AnimeListDbContext())
            {
                Anime anime = db.Animes.Find(id);

                if (anime == null)
                {
                    return RedirectToAction("Index");
                }

                db.Animes.Remove(anime);
                db.SaveChanges();

                return RedirectToAction("Index");
            }
        }
    }
}