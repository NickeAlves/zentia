document.addEventListener('DOMContentLoaded', function () {
  const sections = document.querySelectorAll('.fade-in-section');

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('is-visible');
          observer.unobserve(entry.target);
        }
      });
    },
    {
      threshold: 0,
      rootMargin: '0px 0px -50px 0px',
    }
  );

  sections.forEach((section) => {
    observer.observe(section);
  });
});
